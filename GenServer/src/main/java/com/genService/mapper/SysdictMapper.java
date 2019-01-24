package com.genService.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import pojo.Sysdict;
import pojo.SysdictExample;

public interface SysdictMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int countByExample(SysdictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int deleteByExample(SysdictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int deleteByPrimaryKey(String dictId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int insert(Sysdict record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int insertSelective(Sysdict record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    List<Sysdict> selectByExample(SysdictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    Sysdict selectByPrimaryKey(String dictId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int updateByExampleSelective(@Param("record") Sysdict record, @Param("example") SysdictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int updateByExample(@Param("record") Sysdict record, @Param("example") SysdictExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int updateByPrimaryKeySelective(Sysdict record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sysdict
     *
     * @mbggenerated Tue Feb 27 16:25:22 CST 2018
     */
    int updateByPrimaryKey(Sysdict record);
}