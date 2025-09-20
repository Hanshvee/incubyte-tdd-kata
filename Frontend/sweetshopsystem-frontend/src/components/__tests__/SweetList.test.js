import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import SweetList from '../SweetList';

// Mock axios
jest.mock('axios');
import axios from 'axios';

describe('SweetList Component', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('should display loading state initially', () => {
        axios.get.mockImplementation(() => new Promise(() => { })); // Never resolves

        render(<SweetList />);

        expect(screen.getByText('Loading sweets...')).toBeInTheDocument();
    });

    test('should display sweets when data is loaded', async () => {
        const mockSweets = [
            {
                id: 1,
                name: 'Gulab Jamun',
                description: 'Traditional Indian sweet',
                price: 25.50,
                category_name: 'Traditional',
                stock_quantity: 100,
                image_url: 'https://example.com/gulab.jpg'
            },
            {
                id: 2,
                name: 'Kaju Katli',
                description: 'Cashew-based sweet',
                price: 30.00,
                category_name: 'Traditional',
                stock_quantity: 50,
                image_url: 'https://example.com/kaju.jpg'
            }
        ];

        axios.get.mockResolvedValue({ data: mockSweets });

        render(<SweetList />);

        await waitFor(() => {
            expect(screen.getByText('Gulab Jamun')).toBeInTheDocument();
            expect(screen.getByText('Kaju Katli')).toBeInTheDocument();
            expect(screen.getByText('Traditional Indian sweet')).toBeInTheDocument();
            expect(screen.getByText('Cashew-based sweet')).toBeInTheDocument();
            expect(screen.getByText('₹25.50')).toBeInTheDocument();
            expect(screen.getByText('₹30.00')).toBeInTheDocument();
            expect(screen.getByText('Stock: 100')).toBeInTheDocument();
            expect(screen.getByText('Stock: 50')).toBeInTheDocument();
        });
    });

    test('should display error message when API call fails', async () => {
        axios.get.mockRejectedValue(new Error('API Error'));

        render(<SweetList />);

        await waitFor(() => {
            expect(screen.getByText('Error loading sweets. Please try again.')).toBeInTheDocument();
        });
    });

    test('should display purchase button for sweets with stock', async () => {
        const mockSweets = [
            {
                id: 1,
                name: 'Gulab Jamun',
                description: 'Traditional Indian sweet',
                price: 25.50,
                category_name: 'Traditional',
                stock_quantity: 100,
                image_url: 'https://example.com/gulab.jpg'
            }
        ];

        axios.get.mockResolvedValue({ data: mockSweets });

        render(<SweetList />);

        await waitFor(() => {
            expect(screen.getByText('Purchase')).toBeInTheDocument();
        });
    });

    test('should disable purchase button when stock is zero', async () => {
        const mockSweets = [
            {
                id: 1,
                name: 'Gulab Jamun',
                description: 'Traditional Indian sweet',
                price: 25.50,
                category_name: 'Traditional',
                stock_quantity: 0,
                image_url: 'https://example.com/gulab.jpg'
            }
        ];

        axios.get.mockResolvedValue({ data: mockSweets });

        render(<SweetList />);

        await waitFor(() => {
            const purchaseButton = screen.getByText('Purchase');
            expect(purchaseButton).toBeDisabled();
        });
    });
});
