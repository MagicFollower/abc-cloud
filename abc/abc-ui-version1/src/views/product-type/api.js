import API from '@/utils/api';

export default {
  queryProductType: (params = {}) => API.post('/api/product-type/queryAll', params)
};
