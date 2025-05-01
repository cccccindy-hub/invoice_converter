package com.nnroad.vendor.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nnroad.utils.ExtraAttributeUtils;
import com.nnroad.vendor.domain.SysVendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnroad.vendor.mapper.SysVendorMapper;
import com.nnroad.vendor.service.ISysVendorService;

/**
 * Service implementation for managing SysVendor entities.
 * 
 * Provides the business logic for CRUD operations on SysVendor data.
 * 
 * @author nick
 * @date 2024-10-11
 */
@Service
public class SysVendorServiceImpl implements ISysVendorService 
{
    @Autowired
    private SysVendorMapper sysVendorMapper;
    
    @Autowired
    private ExtraAttributeUtils extraAttributeUtils;


    /**
     * Retrieve a SysVendor by its primary key.
     * 
     * @param id The primary key of the SysVendor
     * @return The SysVendor object corresponding to the provided ID
     */
    @Override
    public SysVendor selectSysVendorById(Long id)
    {
        return sysVendorMapper.selectSysVendorById(id);
    }

    /**
     * Retrieve a list of SysVendor objects based on specified criteria.
     * 
     * @param sysVendor An instance of SysVendor containing the search criteria
     * @return A list of SysVendor objects that match the criteria
     */
    @Override
    public List<SysVendor> selectSysVendorList(SysVendor sysVendor)
    {
        System.out.println(sysVendor.getExtraData());
        Map<String, Object> processedExtraData = extraAttributeUtils.processExtraDataForSearching(sysVendor.getExtraData());
        sysVendor.setExtraData(processedExtraData);
        return sysVendorMapper.selectSysVendorList(sysVendor);
    }

    /**
     * Insert a new SysVendor into the database.
     * 
     * @param sysVendor The SysVendor object to be inserted
     * @return The number of rows affected
     */
    @Override
    public int insertSysVendor(SysVendor sysVendor)
    {
        if (extraAttributeUtils.validateData(sysVendor.getExtraData(), "sys_vendor")) {
            return sysVendorMapper.insertSysVendor(sysVendor);
        } else {
            throw new IllegalArgumentException("Invalid input data for extra attributes");
        }
    }

    /**
     * Update an existing SysVendor in the database.
     * 
     * @param sysVendor The SysVendor object containing updated information
     * @return The number of rows affected
     */
    @Override
    public int updateSysVendor(SysVendor sysVendor)
    {
        if (extraAttributeUtils.validateData(sysVendor.getExtraData(), "sys_vendor")) {
            return sysVendorMapper.updateSysVendor(sysVendor);
        } else {
            throw new IllegalArgumentException("Invalid input data for extra attributes");
        }
    }

    /**
     * Batch delete SysVendor records by their primary keys.
     * 
     * @param ids An array of primary keys of the SysVendor records to be deleted
     * @return The number of rows affected
     */
    @Override
    public int deleteSysVendorByIds(Long[] ids)
    {
        return sysVendorMapper.deleteSysVendorByIds(ids);
    }

    /**
     * Delete a SysVendor by its primary key.
     * 
     * @param id The primary key of the SysVendor to be deleted
     * @return The number of rows affected
     */
    @Override
    public int deleteSysVendorById(Long id)
    {
        return sysVendorMapper.deleteSysVendorById(id);
    }

    @Override
    public List<SysVendor> getVendorByClientId(Long clientId) {
        return sysVendorMapper.getVendorByClientId(clientId);
    }

    @Override
    public List<SysVendor> getVendorByCountries(List<String> countries) {
        return sysVendorMapper.getVendorByCountries(countries);
    }

    @Override
    public List<String> getVendorRegionList() {
        return sysVendorMapper.getVendorRegions();
    }

    private void addVendorWithBusinessTypes(Long vendorId, List<Integer> businessTypeIds) {
        for (Integer businessTypeId: businessTypeIds) {
            sysVendorMapper.insertVendorBusinessType(vendorId, businessTypeId);
        }

    }

    private void deleteBusinessTypesByVendor(Long vendorId) {
        sysVendorMapper.deleteVendorBusinessType(vendorId);
    }

    @Override
    public int deactivateSysVendorById(Long id) {
        return sysVendorMapper.deactivateSysVendorById(id);
    }
}
