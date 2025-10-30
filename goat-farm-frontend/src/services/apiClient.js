import axios from 'axios';

export const apiClient = axios.create({
  baseURL: '/api'
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('gfm_token');
      localStorage.removeItem('gfm_user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export function setAuthToken(token) {
  if (token) {
    apiClient.defaults.headers.common.Authorization = `Bearer ${token}`;
  } else {
    delete apiClient.defaults.headers.common.Authorization;
  }
}

export default apiClient;
