import API from '@/utils/api';

export default {
  queryProductType: (params = {}) => API.get('/api/product-type/queryAll', params)
};
