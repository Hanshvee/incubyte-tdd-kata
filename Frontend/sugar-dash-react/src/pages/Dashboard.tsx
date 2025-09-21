import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { useToast } from '@/hooks/use-toast';
import { Plus, Search, ShoppingCart } from 'lucide-react';
import { SweetCard } from '@/components/SweetCard';
import { PurchaseModal } from '@/components/PurchaseModal';
import { RestockModal } from '@/components/RestockModal';
import { AddSweetModal } from '@/components/AddSweetModal';
import { CartModal, CartItem } from '@/components/CartModal';
import { apiService, Sweet, CreateSweetRequest } from '@/lib/api';

export default function Dashboard() {
  const [sweets, setSweets] = useState<Sweet[]>([]);
  const [filteredSweets, setFilteredSweets] = useState<Sweet[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [sortBy, setSortBy] = useState<string>('name');
  const [loading, setLoading] = useState(true);
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  
  // Modal states
  const [purchaseModal, setPurchaseModal] = useState<{ isOpen: boolean; sweet: Sweet | null }>({
    isOpen: false,
    sweet: null,
  });
  const [restockModal, setRestockModal] = useState<{ isOpen: boolean; sweet: Sweet | null }>({
    isOpen: false,
    sweet: null,
  });
  const [addSweetModal, setAddSweetModal] = useState(false);
  const [cartModal, setCartModal] = useState(false);

  const { toast } = useToast();

  useEffect(() => {
    loadSweets();
  }, []);

  useEffect(() => {
    filterAndSortSweets();
  }, [sweets, searchQuery, sortBy]);

  const loadSweets = async () => {
    try {
      setLoading(true);
      const data = await apiService.getAllSweets();
      setSweets(data);
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load sweets. Please try again.",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  const filterAndSortSweets = () => {
    let filtered = sweets;

    // Filter by search query
    if (searchQuery) {
      filtered = filtered.filter(sweet =>
        sweet.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        sweet.category_name.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // Sort
    filtered = [...filtered].sort((a, b) => {
      switch (sortBy) {
        case 'name':
          return a.name.localeCompare(b.name);
        case 'price-low':
          return a.price - b.price;
        case 'price-high':
          return b.price - a.price;
        case 'stock':
          return b.stock_quantity - a.stock_quantity;
        default:
          return 0;
      }
    });

    setFilteredSweets(filtered);
  };

  const handlePurchase = (sweet: Sweet) => {
    setPurchaseModal({ isOpen: true, sweet });
  };

  const handlePurchaseConfirm = async (sweet: Sweet, quantity: number) => {
    // Only update cart state, do not call backend
    const existingItem = cartItems.find(item => item.sweet.id === sweet.id);
    if (existingItem) {
      const newQuantity = existingItem.quantity + quantity;
      if (newQuantity > sweet.stock_quantity) {
        toast({
          title: "Error",
          description: "Not enough items in stock",
          variant: "destructive",
        });
        return;
      }
      setCartItems(prev =>
        prev.map(item =>
          item.sweet.id === sweet.id
            ? { ...item, quantity: newQuantity }
            : item
        )
      );
    } else {
      setCartItems(prev => [...prev, { sweet, quantity }]);
    }
    toast({
      title: "Added to Cart",
      description: `${quantity} ${sweet.name}(s) added to cart. Please check your cart to place the order.`,
    });
  };

  const handleRestock = (sweet: Sweet) => {
    setRestockModal({ isOpen: true, sweet });
  };

  const handleRestockConfirm = async (sweet: Sweet, quantity: number) => {
    try {
      const response = await apiService.restockSweet(sweet.id, quantity);
      if (response && response.sweet) {
        setSweets(prev => prev.map(s => s.id === sweet.id ? response.sweet : s));
      }
      toast({
        title: response && response.message ? "Success" : "Success",
        description: response && response.message ? response.message : `${sweet.name} restocked with ${quantity} units`,
      });
    } catch (error: any) {
      let errorMsg = "Failed to restock sweet. Please try again.";
      if (error instanceof Error && error.message) {
        try {
          const match = error.message.match(/\{.*\}/);
          if (match) {
            const parsed = JSON.parse(match[0]);
            if (parsed.error) errorMsg = parsed.error;
          }
        } catch (_) {}
      }
      toast({
        title: "Error",
        description: errorMsg,
        variant: "destructive",
      });
    }
  };

  const handleDelete = async (sweet: Sweet) => {
    if (window.confirm(`Are you sure you want to delete ${sweet.name}?`)) {
      try {
        await apiService.deleteSweet(sweet.id);
        setSweets(prev => prev.filter(s => s.id !== sweet.id));
        toast({
          title: "Success",
          description: `${sweet.name} has been deleted`,
        });
      } catch (error: any) {
        let errorMsg = "Failed to delete sweet. Please try again.";
        // Try to extract backend error message if available
        if (error instanceof Error && error.message) {
          // If backend returns JSON error, try to parse it
          try {
            const match = error.message.match(/\{.*\}/);
            if (match) {
              const parsed = JSON.parse(match[0]);
              if (parsed.error) errorMsg = parsed.error;
            }
          } catch (_) {}
        }
        toast({
          title: "Error",
          description: errorMsg,
          variant: "destructive",
        });
      }
    }
  };

  const handleAddSweet = async (sweetData: CreateSweetRequest) => {
  try {
    const newSweet = await apiService.createSweet(sweetData);
    setSweets(prev => [...prev, newSweet]); // ← Add this line
    toast({
      title: "Success",
      description: `${sweetData.name} has been added to your inventory`,
    });
  } catch (error) {
    toast({
      title: "Error",
      description: "Failed to add sweet. Please try again.",
      variant: "destructive",
    });
  }
};


  const handleCartUpdateQuantity = (sweetId: number, newQuantity: number) => {
    const item = cartItems.find(item => item.sweet.id === sweetId);
    if (item && newQuantity > item.sweet.stock_quantity) {
      toast({
        title: "Error",
        description: "Not enough items in stock",
        variant: "destructive",
      });
      return;
    }

    setCartItems(prev => 
      prev.map(item => 
        item.sweet.id === sweetId 
          ? { ...item, quantity: newQuantity }
          : item
      )
    );
  };

  const handleCartRemoveItem = (sweetId: number) => {
    setCartItems(prev => prev.filter(item => item.sweet.id !== sweetId));
  };

  const handleCheckout = async () => {
    try {
      // Process each cart item (call backend to reduce stock)
      for (const item of cartItems) {
        await apiService.purchaseSweet(item.sweet.id, item.quantity);
      }
      // Update local sweet data
      const updatedSweets = await apiService.getAllSweets();
      setSweets(updatedSweets);
      // Clear cart
      setCartItems([]);
      setCartModal(false);
      toast({
        title: "Order Placed Successfully!",
        description: "All items have been purchased and stock updated",
      });
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to process checkout. Please try again.",
        variant: "destructive",
      });
    }
  };

  const totalCartItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Loading sweet treats...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="bg-gradient-primary text-primary-foreground shadow-sweet">
        <div className="container mx-auto px-4 py-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold">Sweet Shop Admin</h1>
              <p className="text-primary-foreground/80">Manage your delicious inventory</p>
            </div>
            <div className="flex items-center gap-4">
              <Button
                onClick={() => setCartModal(true)}
                variant="secondary"
                className="relative"
              >
                <ShoppingCart className="w-4 h-4 mr-2" />
                Cart
                {totalCartItems > 0 && (
                  <span className="absolute -top-2 -right-2 bg-warning text-warning-foreground text-xs rounded-full h-5 w-5 flex items-center justify-center">
                    {totalCartItems}
                  </span>
                )}
              </Button>
              <Button
                onClick={() => setAddSweetModal(true)}
                variant="secondary"
                className="font-medium"
              >
                <Plus className="w-4 h-4 mr-2" />
                Add Sweet
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-4 py-8">
        {/* Search and Filter Bar */}
        <div className="flex flex-col sm:flex-row gap-4 mb-8">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
            <Input
              placeholder="Search sweets by name or category..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
          <Select value={sortBy} onValueChange={setSortBy}>
            <SelectTrigger className="w-full sm:w-48">
              <SelectValue placeholder="Sort by" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="name">Name (A-Z)</SelectItem>
              <SelectItem value="price-low">Price (Low to High)</SelectItem>
              <SelectItem value="price-high">Price (High to Low)</SelectItem>
              <SelectItem value="stock">Stock (High to Low)</SelectItem>
            </SelectContent>
          </Select>
        </div>

        {/* Sweets Grid */}
        {filteredSweets.length === 0 ? (
          <div className="text-center py-12">
            <div className="text-6xl mb-4">🍬</div>
            <h3 className="text-xl font-semibold mb-2">No sweets found</h3>
            <p className="text-muted-foreground mb-4">
              {searchQuery ? 'Try adjusting your search criteria' : 'Start by adding some delicious sweets to your inventory'}
            </p>
            <Button
              onClick={() => setAddSweetModal(true)}
              className="bg-gradient-primary hover:opacity-90"
            >
              <Plus className="w-4 h-4 mr-2" />
              Add Your First Sweet
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredSweets.map((sweet) => (
              <SweetCard
                key={sweet.id}
                sweet={sweet}
                onPurchase={handlePurchase}
                onRestock={handleRestock}
                onDelete={handleDelete}
              />
            ))}
          </div>
        )}
      </main>

      {/* Modals */}
      <PurchaseModal
        sweet={purchaseModal.sweet}
        isOpen={purchaseModal.isOpen}
        onClose={() => setPurchaseModal({ isOpen: false, sweet: null })}
        onConfirm={handlePurchaseConfirm}
      />

      <RestockModal
        sweet={restockModal.sweet}
        isOpen={restockModal.isOpen}
        onClose={() => setRestockModal({ isOpen: false, sweet: null })}
        onConfirm={handleRestockConfirm}
      />

      <AddSweetModal
        isOpen={addSweetModal}
        onClose={() => setAddSweetModal(false)}
        onConfirm={handleAddSweet}
      />

      <CartModal
        isOpen={cartModal}
        onClose={() => setCartModal(false)}
        cartItems={cartItems}
        onUpdateQuantity={handleCartUpdateQuantity}
        onRemoveItem={handleCartRemoveItem}
        onCheckout={handleCheckout}
      />
    </div>
  );
}