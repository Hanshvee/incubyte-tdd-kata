import { useState } from 'react';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { ShoppingCart, Package, Trash2, Plus } from 'lucide-react';
import { Sweet } from '@/lib/api';

interface SweetCardProps {
  sweet: Sweet;
  onPurchase: (sweet: Sweet) => void;
  onRestock: (sweet: Sweet) => void;
  onDelete: (sweet: Sweet) => void;
}

export function SweetCard({ sweet, onPurchase, onRestock, onDelete }: SweetCardProps) {
  const isAvailable = sweet.stock_quantity > 0;

  return (
    <Card className="group hover:shadow-sweet transition-all duration-300 bg-gradient-card border-2 hover:border-primary/20">
      <CardHeader className="pb-3">
        <div className="aspect-square rounded-lg overflow-hidden mb-3 bg-gradient-secondary">
          {sweet.image_url ? (
            <img
              src={sweet.image_url}
              alt={sweet.name}
              className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-accent-foreground">
              <Package size={48} />
            </div>
          )}
        </div>
        <div className="flex items-center justify-between">
          <CardTitle className="text-lg font-bold text-card-foreground truncate">
            {sweet.name}
          </CardTitle>
          <Badge 
            variant={isAvailable ? "default" : "secondary"}
            className={isAvailable ? "bg-success text-success-foreground" : "bg-muted text-muted-foreground"}
          >
            {isAvailable ? "✅ Available" : "❌ Out of Stock"}
          </Badge>
        </div>
      </CardHeader>

      <CardContent className="pb-3">
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-muted-foreground">Category:</span>
            <Badge variant="outline">{sweet.category_name}</Badge>
          </div>
          
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-muted-foreground">Price:</span>
            <span className="text-lg font-bold text-primary">₹{sweet.price}</span>
          </div>
          
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-muted-foreground">Stock:</span>
            <span className={`font-semibold ${sweet.stock_quantity <= 5 ? 'text-warning' : 'text-success'}`}>
              {sweet.stock_quantity} units
            </span>
          </div>
          
          <p className="text-sm text-muted-foreground line-clamp-2 mt-2">
            {sweet.description}
          </p>
        </div>
      </CardContent>

      <CardFooter className="pt-3 gap-2">
        <Button
          onClick={() => onPurchase(sweet)}
          disabled={!isAvailable}
          className="flex-1 bg-gradient-primary hover:opacity-90 text-primary-foreground"
          size="sm"
        >
          <ShoppingCart className="w-4 h-4 mr-1" />
          Purchase
        </Button>
        
        <Button
          onClick={() => onRestock(sweet)}
          variant="outline"
          className="flex-1 border-primary text-primary hover:bg-primary hover:text-primary-foreground"
          size="sm"
        >
          <Plus className="w-4 h-4 mr-1" />
          Restock
        </Button>
        
        <Button
          onClick={() => onDelete(sweet)}
          variant="destructive"
          size="sm"
          className="px-3"
        >
          <Trash2 className="w-4 h-4" />
        </Button>
      </CardFooter>
    </Card>
  );
}