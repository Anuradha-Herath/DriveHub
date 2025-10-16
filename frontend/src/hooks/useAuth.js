import { useAuth } from '../context/AuthContext';

// Custom hook to use authentication
export const useAuthHook = () => {
  return useAuth();
};

export default useAuthHook;
