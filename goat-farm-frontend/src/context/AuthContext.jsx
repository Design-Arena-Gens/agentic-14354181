import { createContext, useEffect, useMemo, useState } from 'react';
import PropTypes from 'prop-types';
import { apiClient, setAuthToken } from '../services/apiClient';

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('gfm_token'));
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('gfm_user');
    return stored ? JSON.parse(stored) : null;
  });

  useEffect(() => {
    if (token) {
      setAuthToken(token);
      localStorage.setItem('gfm_token', token);
    } else {
      setAuthToken(null);
      localStorage.removeItem('gfm_token');
    }
  }, [token]);

  useEffect(() => {
    if (user) {
      localStorage.setItem('gfm_user', JSON.stringify(user));
    } else {
      localStorage.removeItem('gfm_user');
    }
  }, [user]);

  const value = useMemo(() => ({
    token,
    user,
    isAuthenticated: Boolean(token),
    login: async (credentials) => {
      const { data } = await apiClient.post('/auth/login', credentials);
      setToken(data.token);
      setUser({ username: data.username, roles: Array.from(data.roles) });
      return data;
    },
    logout: () => {
      setToken(null);
      setUser(null);
    }
  }), [token, user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

AuthProvider.propTypes = {
  children: PropTypes.node.isRequired
};
