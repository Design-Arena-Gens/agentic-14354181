import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { AuthContext } from './context/AuthContext';
import App from './App';

describe('App routing', () => {
  it('redirects to login when unauthenticated', () => {
    const { getByText } = render(
      <AuthContext.Provider value={{ isAuthenticated: false }}>
        <MemoryRouter initialEntries={[{ pathname: '/' }]}>
          <App />
        </MemoryRouter>
      </AuthContext.Provider>
    );
    expect(getByText(/Goat Farm Management/i)).toBeInTheDocument();
  });
});
