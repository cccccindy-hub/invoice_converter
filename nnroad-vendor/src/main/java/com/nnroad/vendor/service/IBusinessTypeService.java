package com.nnroad.vendor.service;

import java.util.List;
import com.nnroad.vendor.domain.VendorBusinessType;

/**
 * 【Please fill in the function name】 Service Interface
 *
 * @author nick
 * @date 2024-11-25
 */
public interface IBusinessTypeService
{
    /**
     * Query
     *
     * @param id The primary key of the Business Type
     * @return The Business Type object
     */
    public VendorBusinessType selectBusinessTypeById(Long id);

    /**
     * Query for a list of Business Type
     *
     * @param businessType
     * @return A collection of Business Type Object
     */
    public List<VendorBusinessType> selectBusinessTypeList(VendorBusinessType businessType);

    /**
     * Insert a new Business Type
     *
     * @param businessType
     * @return The result
     */
    public int insertBusinessType(VendorBusinessType businessType);

    /**
     * Update a Business Type
     *
     * @param businessType
     * @return The result
     */
    public int updateBusinessType(VendorBusinessType businessType);

    /**
     * Bulk delete Business Types
     *
     * @param ids An array of primary keys of the Business Type
     * @return The result
     */
    public int deleteBusinessTypeByIds(Long[] ids);

    /**
     * Delete a Business Type
     *
     * @param id The primary key of the Business Type
     * @return The result
     */
    public int deleteBusinessTypeById(Long id);
}
