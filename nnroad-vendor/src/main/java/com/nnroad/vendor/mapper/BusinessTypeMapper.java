package com.nnroad.vendor.mapper;

import java.util.List;
import com.nnroad.vendor.domain.VendorBusinessType;

/**
 * Business Type Mapper Interface
 *
 * @author nick
 * @date 2024-11-25
 */
public interface BusinessTypeMapper {
    /**
     * Query for 【Please fill in the function name】
     *
     * @param id The primary key of the 【Please fill in the function name】
     * @return The 【Please fill in the function name】
     */
    public VendorBusinessType selectBusinessTypeById(Long id);

    /**
     * Query for a list of 【Please fill in the function name】
     *
     * @param businessType The 【Please fill in the function name】
     * @return A collection of 【Please fill in the function name】
     */
    public List<VendorBusinessType> selectBusinessTypeList(VendorBusinessType businessType);

    /**
     * Insert a new 【Please fill in the function name】
     *
     * @param businessType The 【Please fill in the function name】
     * @return The result
     */
    public int insertBusinessType(VendorBusinessType businessType);

    /**
     * Update a 【Please fill in the function name】
     *
     * @param businessType The 【Please fill in the function name】
     * @return The result
     */
    public int updateBusinessType(VendorBusinessType businessType);

    /**
     * Delete a 【Please fill in the function name】
     *
     * @param id The primary key of the 【Please fill in the function name】
     * @return The result
     */
    public int deleteBusinessTypeById(Long id);

    /**
     * Bulk delete 【Please fill in the function name】
     *
     * @param ids An array of primary keys of the data to be deleted
     * @return The result
     */
    public int deleteBusinessTypeByIds(Long[] ids);
}