import API from '@/utils/api';
import productTypeAPI from '@/views/product-type/api';

export default {
  queryProductInfo: (params = {}) => API.post('/api/product-info/queryAll', params),
  queryProductType: (params = {}) => productTypeAPI.queryProductType(params),
};
