import request from '@/utils/request';

interface VendorQuery {
  page?: number;
  pageSize?: number;
  [key: string]: any; // to allow other dynamic fields
}

interface VendorData {
  id?: string;
  vendorName: string;
  [key: string]: any;
}

interface CountryData {
  countries: string[]; // An array of country codes or names
}

// Get list of vendors
export function listVendor(query: VendorQuery): Promise<any> {
  return request({
    url: '/system/vendor/list',
    method: 'get',
    params: query,
  });
}

// Get vendors list by client ID
export function listVendorsByClientId(clientId: string): Promise<any> {
  return request({
    url: `/system/client/${clientId}/vendors`,
    method: 'get',
  });
}

// Get detailed information of a vendor
export function getVendor(id: string): Promise<any> {
  return request({
    url: `/system/vendor/${id}`,
    method: 'get',
  });
}

// Add a new vendor
export function addVendor(data: VendorData): Promise<any> {
  return request({
    url: '/system/vendor',
    method: 'post',
    data: data,
  });
}

// Update vendor information
export function updateVendor(data: VendorData): Promise<any> {
  return request({
    url: '/system/vendor',
    method: 'put',
    data: data,
  });
}

// Delete a vendor
export function delVendor(id: string): Promise<any> {
  return request({
    url: `/system/vendor/${id}`,
    method: 'delete',
  });
}

// Get vendors by country
export function getVendorsByCountry(data: CountryData): Promise<any> {
  return request({
    url: '/system/vendor/countries',
    method: 'post',
    data: data,
  });
}

// Get vendor regions list
export function listVendorRegions(): Promise<any> {
  return request({
    url: '/system/vendor/region/list',
    method: 'get',
  });


}

// deactivate client function
export function deactivateVendor(id: string): Promise<any> {
  return request({
    url: `/system/vendor/${id}/deactivate`,
    method: 'put'
  });
}
