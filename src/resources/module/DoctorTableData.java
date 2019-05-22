package resources.module;

import javafx.beans.property.SimpleStringProperty;

public class DoctorTableData {
    private SimpleStringProperty KSMC;
    private SimpleStringProperty YSBH;
    private SimpleStringProperty YSMC;
    private SimpleStringProperty HZLX;
    private SimpleStringProperty GHRC;
    private SimpleStringProperty SRHJ;

    public DoctorTableData(String KSMC, String YSBH, String YSMC, String HZLX, String GHRC, String SRHJ) {
        this.KSMC = new SimpleStringProperty(KSMC);
        this.YSBH = new SimpleStringProperty(YSBH);
        this.YSMC = new SimpleStringProperty(YSMC);
        this.HZLX = new SimpleStringProperty(HZLX);
        this.GHRC = new SimpleStringProperty(GHRC);
        this.SRHJ = new SimpleStringProperty(SRHJ);
    }

    public String getKSMC() {
        return KSMC.get();
    }

    public SimpleStringProperty KSMCProperty() {
        return KSMC;
    }

    public void setKSMC(String KSMC) {
        this.KSMC.set(KSMC);
    }

    public String getYSBH() {
        return YSBH.get();
    }

    public SimpleStringProperty YSBHProperty() {
        return YSBH;
    }

    public void setYSBH(String YSBH) {
        this.YSBH.set(YSBH);
    }

    public String getYSMC() {
        return YSMC.get();
    }

    public SimpleStringProperty YSMCProperty() {
        return YSMC;
    }

    public void setYSMC(String YSMC) {
        this.YSMC.set(YSMC);
    }

    public String getHZLX() {
        return HZLX.get();
    }

    public SimpleStringProperty HZLXProperty() {
        return HZLX;
    }

    public void setHZLX(String HZLX) {
        this.HZLX.set(HZLX);
    }

    public String getGHRC() {
        return GHRC.get();
    }

    public SimpleStringProperty GHRCProperty() {
        return GHRC;
    }

    public void setGHRC(String GHRC) {
        this.GHRC.set(GHRC);
    }

    public String getSRHJ() {
        return SRHJ.get();
    }

    public SimpleStringProperty SRHJProperty() {
        return SRHJ;
    }

    public void setSRHJ(String SRHJ) {
        this.SRHJ.set(SRHJ);
    }
}
