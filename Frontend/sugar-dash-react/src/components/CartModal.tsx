import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { ShoppingCart, Minus, Plus, X } from 'lucide-react';
import { Sweet } from '@/lib/api';

export interface CartItem {
  sweet: Sweet;
  quantity: number;
}

interface CartModalProps {
  isOpen: boolean;
  onClose: () => void;
  cartItems: CartItem[];
  onUpdateQuantity: (sweetId: number, newQuantity: number) => void;
  onRemoveItem: (sweetId: number) => void;
  onCheckout: () => void;
}

export function CartModal({ 
  isOpen, 
  onClose, 
  cartItems, 
  onUpdateQuantity, 
  onRemoveItem, 
  onCheckout 
}: CartModalProps) {
  const totalAmount = cartItems.reduce((sum, item) => sum + (item.sweet.price * item.quantity), 0);
  const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md max-h-[80vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <ShoppingCart className="w-5 h-5 text-primary" />
            Shopping Cart ({totalItems} items)
          </DialogTitle>
          <DialogDescription>
            Review your items before checkout
          </DialogDescription>
        </DialogHeader>
        
        <div className="space-y-4 py-4">
          {cartItems.length === 0 ? (
            <div className="text-center py-8">
              <ShoppingCart className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">Your cart is empty</p>
            </div>
          ) : (
            <>
              {cartItems.map((item) => (
                <div key={item.sweet.id} className="flex items-center gap-3 p-3 border rounded-lg">
                  <div className="w-12 h-12 rounded bg-gradient-secondary flex items-center justify-center flex-shrink-0">
                    {item.sweet.image_url ? (
                      <img 
                        src={item.sweet.image_url} 
                        alt={item.sweet.name}
                        className="w-full h-full object-cover rounded"
                      />
                    ) : (
                      <ShoppingCart className="w-6 h-6 text-accent-foreground" />
                    )}
                  </div>
                  
                  <div className="flex-1 min-w-0">
                    <h4 className="font-medium truncate">{item.sweet.name}</h4>
                    <p className="text-sm text-muted-foreground">₹{item.sweet.price} each</p>
                  </div>
                  
                  <div className="flex items-center gap-2">
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => onUpdateQuantity(item.sweet.id, Math.max(1, item.quantity - 1))}
                      className="h-8 w-8 p-0"
                    >
                      <Minus className="w-3 h-3" />
                    </Button>
                    
                    <span className="w-8 text-center font-medium">{item.quantity}</span>
                    
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => onUpdateQuantity(item.sweet.id, item.quantity + 1)}
                      disabled={item.quantity >= item.sweet.stock_quantity}
                      className="h-8 w-8 p-0"
                    >
                      <Plus className="w-3 h-3" />
                    </Button>
                    
                    <Button
                      size="sm"
                      variant="destructive"
                      onClick={() => onRemoveItem(item.sweet.id)}
                      className="h-8 w-8 p-0 ml-2"
                    >
                      <X className="w-3 h-3" />
                    </Button>
                  </div>
                  
                  <div className="text-right font-medium">
                    ₹{(item.sweet.price * item.quantity).toFixed(2)}
                  </div>
                </div>
              ))}
              
              <div className="border-t pt-4">
                <div className="flex justify-between items-center text-lg font-bold">
                  <span>Total Amount:</span>
                  <span className="text-primary">₹{totalAmount.toFixed(2)}</span>
                </div>
              </div>
            </>
          )}
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={onClose}>
            Continue Shopping
          </Button>
          {cartItems.length > 0 && (
            <Button 
              onClick={onCheckout}
              className="bg-gradient-primary hover:opacity-90"
            >
              Checkout
            </Button>
          )}
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}