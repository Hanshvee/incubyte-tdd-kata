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
import { Package } from 'lucide-react';
import { Sweet } from '@/lib/api';

interface RestockModalProps {
  sweet: Sweet | null;
  isOpen: boolean;
  onClose: () => void;
  onConfirm: (sweet: Sweet, quantity: number) => void;
}

export function RestockModal({ sweet, isOpen, onClose, onConfirm }: RestockModalProps) {
  const [quantity, setQuantity] = useState(1);
  const [error, setError] = useState('');

  const handleConfirm = () => {
    if (!sweet) return;
    
    if (quantity <= 0) {
      setError('Quantity must be greater than 0');
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
            <Package className="w-5 h-5 text-primary" />
            Restock {sweet.name}
          </DialogTitle>
          <DialogDescription>
            Enter the quantity to add to stock. Current stock: {sweet.stock_quantity} units
          </DialogDescription>
        </DialogHeader>
        
        <div className="space-y-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="restockQuantity" className="text-right">
              Quantity
            </Label>
            <div className="col-span-3">
              <Input
                id="restockQuantity"
                type="number"
                min="1"
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
              <span className="font-medium">New Stock Total:</span>
              <span className="text-lg font-bold text-success">
                {sweet.stock_quantity + quantity} units
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
            className="bg-success hover:bg-success/90 text-success-foreground"
          >
            Confirm Restock
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}