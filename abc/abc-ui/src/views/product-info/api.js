import API from '@/utils/api';

export default {
  queryProductInfo: (params = {}) => API.post('/api/product-info/queryAll', params)
};
