package com.nnroad.vendor.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnroad.vendor.mapper.BusinessTypeMapper;
import com.nnroad.vendor.domain.VendorBusinessType;
import com.nnroad.vendor.service.IBusinessTypeService;

/**
 * Service - Business Layer Implementation
 *
 * @author ruoyi
 * @date 2024-11-25
 */
@Service
public class BusinessTypeServiceImpl implements IBusinessTypeService
{
    @Autowired
    private BusinessTypeMapper businessTypeMapper;

    /**
     * Query
     *
     * @param id The primary key of the Business Type
     * @return The Business Type object
     */
    @Override
    public VendorBusinessType selectBusinessTypeById(Long id)
    {
        return businessTypeMapper.selectBusinessTypeById(id);
    }

    /**
     * Query for a list of Business Type
     *
     * @param businessType
     * @return A collection of Business Type Object
     */
    @Override
    public List<VendorBusinessType> selectBusinessTypeList(VendorBusinessType businessType)
    {
        return businessTypeMapper.selectBusinessTypeList(businessType);
    }

    /**
     * Insert a new Business Type
     *
     * @param businessType
     * @return The result
     */
    @Override
    public int insertBusinessType(VendorBusinessType businessType)
    {
        return businessTypeMapper.insertBusinessType(businessType);
    }

    /**
     * Update a Business Type
     *
     * @param businessType
     * @return The result
     */
    @Override
    public int updateBusinessType(VendorBusinessType businessType)
    {
        return businessTypeMapper.updateBusinessType(businessType);
    }

    /**
     * Bulk delete Business Types
     *
     * @param ids An array of primary keys of the Business Type
     * @return The result
     */
    @Override
    public int deleteBusinessTypeByIds(Long[] ids)
    {
        return businessTypeMapper.deleteBusinessTypeByIds(ids);
    }

    /**
     * Delete a Business Type
     *
     * @param id The primary key of the Business Type
     * @return The result
     */
    @Override
    public int deleteBusinessTypeById(Long id)
    {
        return businessTypeMapper.deleteBusinessTypeById(id);
    }
}
