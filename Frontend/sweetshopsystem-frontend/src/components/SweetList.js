import React, { useState, useEffect } from 'react';
import axios from 'axios';

const SweetList = () => {
  const [sweets, setSweets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const fetchSweets = async () => {
      try {
        const response = await axios.get('/api/sweets');
        setSweets(response.data);
      } catch (err) {
        setError(true);
      } finally {
        setLoading(false);
      }
    };

    fetchSweets();
  }, []);

  const handlePurchase = (id) => {
    // Implementation will come later
  };

  if (loading) {
    return <div>Loading sweets...</div>;
  }

  if (error) {
    return <div>Error loading sweets. Please try again.</div>;
  }

  return (
    <div className="sweet-list">
      {sweets.map((sweet) => (
        <div key={sweet.id} className="sweet-item">
          <h3>{sweet.name}</h3>
          <p>{sweet.description}</p>
          <p>₹{sweet.price.toFixed(2)}</p>
          <p>Stock: {sweet.stock_quantity}</p>
          <button 
            disabled={sweet.stock_quantity === 0} 
            onClick={() => handlePurchase(sweet.id)}
          >
            Purchase
          </button>
        </div>
      ))}
    </div>
  );
};

export default SweetList;