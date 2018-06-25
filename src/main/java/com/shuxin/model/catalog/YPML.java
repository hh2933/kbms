package com.shuxin.model.catalog;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.shuxin.mapper.catalog.YPMLMapper;
import sun.applet.Main;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zengj on 2018-6-6.
 */
@Entity
@Table(name = "M_YPML", schema = "KBMS", catalog = "药品目录")
@TableName("M_YPML")
public class YPML implements Serializable{

    private static final long serialVersionUID = 6371373733882172072L;

    @TableId(type = IdType.UUID)
    private String id;
    private String ybypbm;  //医保药品编码
    private String yyypbm;  //医院药品编码
    private String yyypmc;  //医院药品名称
    private Integer yplx;   //药品类别(1:西药,2:中成药,3:中药饮片)
    private String jx;      //剂型,比如咀嚼片/注射用粉针/普通片剂等
    private String gg;      //规格
    private String dw;      //单位
    private BigDecimal jg;      //价格
    private Integer zxbz;   //最小包装
    private String sccs;    //生产厂商
    private Integer zfbl;   //自付比例
    private String gytj;    //给药途径
    private String sm;      //说明
    private Date drsj;      //导入时间
    private Integer jxzt;   //解析状态(1：未开始，2已解析，3：全部解析，4：部分解析，5：全部未能解析,6:手动解析)
    private String jxjg;    //解析结果(规则表1ID.ID;规则表2ID.ID)
    private String jxjgms;    //解析结果描述(规则表1名称;规则表2名称)
    private String yjxsm;   //已解析说明
    private String wnjxsm;  //未能解析说明

    /*
     * @author zengj
     * @Description 获取解析状态描述
     * @Date 2018-6-19 12:24
     * @Param [jxzt]
     * @return java.lang.String
     */
    public String getJxztMs(){
        switch (this.jxzt){
            case 1:return "未开始";
            case 2:return "已解析";
            case 3:return "全部解析";
            case 4:return "部分解析";
            case 5:return "全部未能解析";
            case 6:return "手动解析";
            default:return "";
        }
    }

    @Id
    @Column(name = "ID", nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "YBYPBM", nullable = true, length = 32)
    public String getYbypbm() {
        return ybypbm;
    }

    public void setYbypbm(String ybypbm) {
        this.ybypbm = ybypbm;
    }

    @Basic
    @Column(name = "YYYPBM", nullable = true, length = 32)
    public String getYyypbm() {
        return yyypbm;
    }

    public void setYyypbm(String yyypbm) {
        this.yyypbm = yyypbm;
    }

    @Basic
    @Column(name = "YYYPMC", nullable = false, length = 100)
    public String getYyypmc() {
        return yyypmc;
    }

    public void setYyypmc(String yyypmc) {
        this.yyypmc = yyypmc;
    }

    @Basic
    @Column(name = "YPLX", nullable = false, precision = 0)
    public Integer getYplx() {
        return yplx;
    }

    public void setYplx(Integer yplx) {
        this.yplx = yplx;
    }

    @Basic
    @Column(name = "JX", nullable = false, length = 50)
    public String getJx() {
        return jx;
    }

    public void setJx(String jx) {
        this.jx = jx;
    }

    @Basic
    @Column(name = "GG", nullable = false, length = 100)
    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    @Basic
    @Column(name = "DW", nullable = false, length = 10)
    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    @Basic
    @Column(name = "JG", nullable = false, precision = 4)
    public BigDecimal getJg() {
        return jg;
    }

    public void setJg(BigDecimal jg) {
        this.jg = jg;
    }

    @Basic
    @Column(name = "ZXBZ", nullable = false, precision = 0)
    public Integer getZxbz() {
        return zxbz;
    }

    public void setZxbz(Integer zxbz) {
        this.zxbz = zxbz;
    }

    @Basic
    @Column(name = "SCCS", nullable = false, length = 100)
    public String getSccs() {
        return sccs;
    }

    public void setSccs(String sccs) {
        this.sccs = sccs;
    }

    @Basic
    @Column(name = "ZFBL", nullable = false, precision = 0)
    public Integer getZfbl() {
        return zfbl;
    }

    public void setZfbl(Integer zfbl) {
        this.zfbl = zfbl;
    }

    @Basic
    @Column(name = "GYTJ", nullable = true, length = 50)
    public String getGytj() {
        return gytj;
    }

    public void setGytj(String gytj) {
        this.gytj = gytj;
    }

    @Basic
    @Column(name = "SM", nullable = true, length = 255)
    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    @Basic
    @Column(name = "DRSJ", nullable = false)
    public Date getDrsj() {
        return drsj;
    }

    public void setDrsj(Date drsj) {
        this.drsj = drsj;
    }

    @Basic
    @Column(name = "JXZT", nullable = true, precision = 0)
    public Integer getJxzt() {
        return jxzt;
    }

    public void setJxzt(Integer jxzt) {
        this.jxzt = jxzt;
    }

    @Basic
    @Column(name = "JXJG", nullable = true, length = 400)
    public String getJxjg() {
        return jxjg;
    }

    public void setJxjg(String jxjg) {
        this.jxjg = jxjg;
    }

    @Basic
    @Column(name = "JXJGMS", nullable = true, length = 255)
    public String getJxjgms() {
        return jxjgms;
    }

    public void setJxjgms(String jxjgms) {
        this.jxjgms = jxjgms;
    }

    @Basic
    @Column(name = "YJXSM", nullable = true, length = 255)
    public String getYjxsm() {
        return yjxsm;
    }

    public void setYjxsm(String yjxsm) {
        this.yjxsm = yjxsm;
    }

    @Basic
    @Column(name = "WNJXSM", nullable = true, length = 255)
    public String getWnjxsm() {
        return wnjxsm;
    }

    public void setWnjxsm(String wnjxsm) {
        this.wnjxsm = wnjxsm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YPML ypml = (YPML) o;

        if (id != null ? !id.equals(ypml.id) : ypml.id != null) return false;
        if (ybypbm != null ? !ybypbm.equals(ypml.ybypbm) : ypml.ybypbm != null) return false;
        if (yyypbm != null ? !yyypbm.equals(ypml.yyypbm) : ypml.yyypbm != null) return false;
        if (yyypmc != null ? !yyypmc.equals(ypml.yyypmc) : ypml.yyypmc != null) return false;
        if (yplx != null ? !yplx.equals(ypml.yplx) : ypml.yplx != null) return false;
        if (jx != null ? !jx.equals(ypml.jx) : ypml.jx != null) return false;
        if (gg != null ? !gg.equals(ypml.gg) : ypml.gg != null) return false;
        if (dw != null ? !dw.equals(ypml.dw) : ypml.dw != null) return false;
        if (jg != null ? !jg.equals(ypml.jg) : ypml.jg != null) return false;
        if (zxbz != null ? !zxbz.equals(ypml.zxbz) : ypml.zxbz != null) return false;
        if (sccs != null ? !sccs.equals(ypml.sccs) : ypml.sccs != null) return false;
        if (zfbl != null ? !zfbl.equals(ypml.zfbl) : ypml.zfbl != null) return false;
        if (gytj != null ? !gytj.equals(ypml.gytj) : ypml.gytj != null) return false;
        if (sm != null ? !sm.equals(ypml.sm) : ypml.sm != null) return false;
        if (drsj != null ? !drsj.equals(ypml.drsj) : ypml.drsj != null) return false;
        if (jxzt != null ? !jxzt.equals(ypml.jxzt) : ypml.jxzt != null) return false;
        if (jxjg != null ? !jxjg.equals(ypml.jxjg) : ypml.jxjg != null) return false;
        if (yjxsm != null ? !yjxsm.equals(ypml.yjxsm) : ypml.yjxsm != null) return false;
        if (wnjxsm != null ? !wnjxsm.equals(ypml.wnjxsm) : ypml.wnjxsm != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (ybypbm != null ? ybypbm.hashCode() : 0);
        result = 31 * result + (yyypbm != null ? yyypbm.hashCode() : 0);
        result = 31 * result + (yyypmc != null ? yyypmc.hashCode() : 0);
        result = 31 * result + (yplx != null ? yplx.hashCode() : 0);
        result = 31 * result + (jx != null ? jx.hashCode() : 0);
        result = 31 * result + (gg != null ? gg.hashCode() : 0);
        result = 31 * result + (dw != null ? dw.hashCode() : 0);
        result = 31 * result + (jg != null ? jg.hashCode() : 0);
        result = 31 * result + (zxbz != null ? zxbz.hashCode() : 0);
        result = 31 * result + (sccs != null ? sccs.hashCode() : 0);
        result = 31 * result + (zfbl != null ? zfbl.hashCode() : 0);
        result = 31 * result + (gytj != null ? gytj.hashCode() : 0);
        result = 31 * result + (sm != null ? sm.hashCode() : 0);
        result = 31 * result + (drsj != null ? drsj.hashCode() : 0);
        result = 31 * result + (jxzt != null ? jxzt.hashCode() : 0);
        result = 31 * result + (jxjg != null ? jxjg.hashCode() : 0);
        result = 31 * result + (yjxsm != null ? yjxsm.hashCode() : 0);
        result = 31 * result + (wnjxsm != null ? wnjxsm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "YPML{" +
                "id='" + id + '\'' +
                ", ybypbm='" + ybypbm + '\'' +
                ", yyypbm='" + yyypbm + '\'' +
                ", yyypmc='" + yyypmc + '\'' +
                ", yplx=" + yplx +
                ", jx='" + jx + '\'' +
                ", gg='" + gg + '\'' +
                ", dw='" + dw + '\'' +
                ", jg=" + jg +
                ", zxbz=" + zxbz +
                ", sccs='" + sccs + '\'' +
                ", zfbl=" + zfbl +
                ", gytj='" + gytj + '\'' +
                ", sm='" + sm + '\'' +
                ", drsj=" + drsj +
                ", jxzt=" + jxzt +
                ", jxjg='" + jxjg + '\'' +
                ", yjxsm='" + yjxsm + '\'' +
                ", wnjxsm='" + wnjxsm + '\'' +
                '}';
    }


}
