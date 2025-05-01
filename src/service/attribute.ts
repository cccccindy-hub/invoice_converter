import request from '@/utils/request';

// Define types for the parameters
interface AttributeQuery {
  page?: number;
  pageSize?: number;
  [key: string]: any; // to allow other dynamic fields
}

interface AttributeData {
  id?: string;
  name: string;
  type: string;
}

// Get list of extra attributes in hierarchy
export function listExtraAttributesInHierarchy(tableName: string): Promise<any> {
  return request({
    url: `/system/attribute/listInHierarchy/${tableName}`,
    method: 'get',
  });
}

// Get list of extra attributes
export function listAttribute(query: AttributeQuery): Promise<any> {
  return request({
    url: '/system/attribute/list',
    method: 'get',
    params: query,
  });
}

// Get list of parent attributes
export function listParentAttribute(tableName: string): Promise<any> {
  return request({
    url: `/system/attribute/parentList/${tableName}`,
    method: 'get',
  });
}

// Get detailed information of an attribute
export function getAttribute(id: string, tableName: string): Promise<any> {
  return request({
    url: `/system/attribute/${tableName}/${id}`,
    method: 'get',
  });
}

// Add a new attribute
export function addAttribute(data: AttributeData): Promise<any> {
  return request({
    url: '/system/attribute',
    method: 'post',
    data: data,
  });
}

// Modify an existing attribute
export function updateAttribute(data: AttributeData): Promise<any> {
  return request({
    url: '/system/attribute/',
    method: 'put',
    data: data,
  });
}

// Delete an attribute
export function delAttribute(id: string, tableName: string): Promise<any> {
  return request({
    url: `/system/attribute/${tableName}/${id}`,
    method: 'delete',
  });
}
