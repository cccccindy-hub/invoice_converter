package com.nnroad.vendor.service;

import com.nnroad.vendor.domain.SysVendor;
import java.util.List;

/**
 * Service interface for managing SysVendor entities.
 * 
 * Provides methods for CRUD operations on vendor data.
 * 
 * @author nick
 * @date 2024-10-11
 */
public interface ISysVendorService 
{
    /**
     * Retrieve a SysVendor by its primary key.
     * 
     * @param id The primary key of the SysVendor
     * @return The SysVendor object corresponding to the provided ID
     */
    public SysVendor selectSysVendorById(Long id);

    /**
     * Retrieve a list of SysVendor objects based on specified criteria.
     * 
     * @param sysVendor An instance of SysVendor containing the search criteria
     * @return A list of SysVendor objects that match the criteria
     */
    public List<SysVendor> selectSysVendorList(SysVendor sysVendor);

    /**
     * Insert a new SysVendor into the database.
     * 
     * @param sysVendor The SysVendor object to be inserted
     * @return The number of rows affected
     */
    public int insertSysVendor(SysVendor sysVendor);

    /**
     * Update an existing SysVendor in the database.
     * 
     * @param sysVendor The SysVendor object containing updated information
     * @return The number of rows affected
     */
    public int updateSysVendor(SysVendor sysVendor);

    /**
     * Batch delete SysVendor records by their primary keys.
     * 
     * @param ids An array of primary keys of the SysVendor records to be deleted
     * @return The number of rows affected
     */
    public int deleteSysVendorByIds(Long[] ids);

    /**
     * Delete a SysVendor by its primary key.
     * 
     * @param id The primary key of the SysVendor to be deleted
     * @return The number of rows affected
     */
    public int deleteSysVendorById(Long id);

    /**
     * Get vendors by clinet id
     * 
     * @param id Client primary key
     * @return List of vendor objects
     */
	public List<SysVendor> getVendorByClientId(Long clientId);

	
	public List<SysVendor> getVendorByCountries(List<String> countries);

    List<String> getVendorRegionList();

    public int deactivateSysVendorById(Long id);
}
