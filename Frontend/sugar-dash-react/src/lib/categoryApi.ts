export interface Category {
  category_id: number;
  name: string;
}

export async function fetchCategories(): Promise<Category[]> {
  const response = await fetch('/api/categories');
  if (!response.ok) throw new Error('Failed to fetch categories');
  return response.json();
}
