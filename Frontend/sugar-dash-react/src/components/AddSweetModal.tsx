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
import { Textarea } from '@/components/ui/textarea';
import { Plus } from 'lucide-react';
import { CreateSweetRequest } from '@/lib/api';

interface AddSweetModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: (sweet: CreateSweetRequest) => void;
}

export function AddSweetModal({ isOpen, onClose, onConfirm }: AddSweetModalProps) {
  const [formData, setFormData] = useState<CreateSweetRequest>({
    name: '',
    category_id: 0,
    description: '',
    price: 0,
    stock_quantity: 0,
    image_url: '',
  });
  const [errors, setErrors] = useState<Record<string, string>>({});

  const validateForm = () => {
    const newErrors: Record<string, string> = {};
    
    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
    }
    
    if (!formData.category_id || formData.category_id <= 0) {
      newErrors.category_id = 'Category ID is required';
    }
    
    if (!formData.description.trim()) {
      newErrors.description = 'Description is required';
    }
    
    if (formData.price <= 0) {
      newErrors.price = 'Price must be greater than 0';
    }
    
    if (formData.stock_quantity < 0) {
      newErrors.stock_quantity = 'Stock cannot be negative';
    }
    
    if (formData.image_url && !isValidUrl(formData.image_url)) {
      newErrors.image_url = 'Please enter a valid URL';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const isValidUrl = (string: string) => {
    try {
      new URL(string);
      return true;
    } catch (_) {
      return false;
    }
  };

  const handleSubmit = () => {
    if (validateForm()) {
      onConfirm(formData);
      handleClose();
    }
  };

  const handleClose = () => {
    setFormData({
      name: '',
      category_id: 0,
      description: '',
      price: 0,
      stock_quantity: 0,
      image_url: '',
    });
    setErrors({});
    onClose();
  };

  const handleInputChange = (field: keyof CreateSweetRequest, value: string | number) => {
    setFormData(prev => ({ ...prev, [field]: value }));
    if (errors[field]) {
      setErrors(prev => ({ ...prev, [field]: '' }));
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-md max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Plus className="w-5 h-5 text-primary" />
            Add New Sweet
          </DialogTitle>
          <DialogDescription>
            Fill in the details to add a new sweet to your inventory.
          </DialogDescription>
        </DialogHeader>
        
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="name">Name *</Label>
            <Input
              id="name"
              value={formData.name}
              onChange={(e) => handleInputChange('name', e.target.value)}
              placeholder="e.g., Chocolate Truffle"
            />
            {errors.name && <p className="text-sm text-destructive">{errors.name}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="category_id">Category ID *</Label>
            <Input
              id="category_id"
              type="number"
              min="1"
              value={formData.category_id}
              onChange={(e) => handleInputChange('category_id', Number(e.target.value))}
              placeholder="e.g., 1, 2, 3"
            />
            {errors.category_id && <p className="text-sm text-destructive">{errors.category_id}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="price">Price (₹) *</Label>
            <Input
              id="price"
              type="number"
              min="0"
              step="0.01"
              value={formData.price}
              onChange={(e) => handleInputChange('price', Number(e.target.value))}
              placeholder="0.00"
            />
            {errors.price && <p className="text-sm text-destructive">{errors.price}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="stock_quantity">Initial Stock *</Label>
            <Input
              id="stock_quantity"
              type="number"
              min="0"
              value={formData.stock_quantity}
              onChange={(e) => handleInputChange('stock_quantity', Number(e.target.value))}
              placeholder="0"
            />
            {errors.stock_quantity && <p className="text-sm text-destructive">{errors.stock_quantity}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Description *</Label>
            <Textarea
              id="description"
              value={formData.description}
              onChange={(e) => handleInputChange('description', e.target.value)}
              placeholder="Describe your sweet..."
              rows={3}
            />
            {errors.description && <p className="text-sm text-destructive">{errors.description}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="image_url">Image URL (optional)</Label>
            <Input
              id="image_url"
              type="url"
              value={formData.image_url}
              onChange={(e) => handleInputChange('image_url', e.target.value)}
              placeholder="https://example.com/image.jpg"
            />
            {errors.image_url && <p className="text-sm text-destructive">{errors.image_url}</p>}
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={handleClose}>
            Cancel
          </Button>
          <Button 
            onClick={handleSubmit}
            className="bg-gradient-primary hover:opacity-90"
          >
            Add Sweet
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}