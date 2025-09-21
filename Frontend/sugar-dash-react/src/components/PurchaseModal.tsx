import { useState } from 'react';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { ShoppingCart } from 'lucide-react';
import { Sweet } from '@/lib/api';

interface PurchaseModalProps {
  sweet: Sweet | null;
  isOpen: boolean;
  onClose: () => void;
  onConfirm: (sweet: Sweet, quantity: number) => void;
}

export function PurchaseModal({ sweet, isOpen, onClose, onConfirm }: PurchaseModalProps) {
  const [quantity, setQuantity] = useState(1);
  const [error, setError] = useState('');

  const handleConfirm = () => {
    if (!sweet) return;
    
    if (quantity <= 0) {
      setError('Quantity must be greater than 0');
      return;
    }
    
    if (quantity > sweet.stock_quantity) {
      setError('Not enough items in stock');
      return;
    }
    
    onConfirm(sweet, quantity);
    setQuantity(1);
    setError('');
    onClose();
  };

  const handleClose = () => {
    setQuantity(1);
    setError('');
    onClose();
  };

  if (!sweet) return null;

  return (
    <Dialog open={isOpen} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <ShoppingCart className="w-5 h-5 text-primary" />
            Purchase {sweet.name}
          </DialogTitle>
          <DialogDescription>
            Enter the quantity you want to purchase. Available stock: {sweet.stock_quantity} units
          </DialogDescription>
        </DialogHeader>
        
        <div className="space-y-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="quantity" className="text-right">
              Quantity
            </Label>
            <div className="col-span-3">
              <Input
                id="quantity"
                type="number"
                min="1"
                max={sweet.stock_quantity}
                value={quantity}
                onChange={(e) => {
                  setQuantity(Number(e.target.value));
                  setError('');
                }}
                className="w-full"
              />
              {error && (
                <p className="text-sm text-destructive mt-1">{error}</p>
              )}
            </div>
          </div>
          
          <div className="bg-gradient-secondary p-3 rounded-lg">
            <div className="flex justify-between items-center">
              <span className="font-medium">Total Amount:</span>
              <span className="text-lg font-bold text-primary">
                ₹{(sweet.price * quantity).toFixed(2)}
              </span>
            </div>
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={handleClose}>
            Cancel
          </Button>
          <Button 
            onClick={handleConfirm}
            className="bg-gradient-primary hover:opacity-90"
          >
            Add to Cart
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}