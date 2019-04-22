package resources;

import javafx.beans.property.SimpleStringProperty;

public class PatientTableData {
    private SimpleStringProperty GHBH;
    private SimpleStringProperty BRMC;
    private SimpleStringProperty GHRQ;
    private SimpleStringProperty HZLX;

    public PatientTableData(String GHBH, String BRMC, String GHRQ, String HZLX) {
        this.GHBH = new SimpleStringProperty(GHBH);
        this.BRMC = new SimpleStringProperty(BRMC);
        this.GHRQ = new SimpleStringProperty(GHRQ);
        this.HZLX = new SimpleStringProperty(HZLX);
    }

    public void setGHBH(String GHBH) {
        this.GHBH.set(GHBH);
    }

    public void setBRMC(String BRMC) {
        this.BRMC.set(BRMC);
    }

    public void setGHRQ(String GHRQ) {
        this.GHRQ.set(GHRQ);
    }

    public void setHZLX(String HZLX) {
        this.HZLX.set(HZLX);
    }


    public String getGHBH() {
        return GHBH.get();
    }

    public SimpleStringProperty GHBHProperty() {
        return GHBH;
    }

    public String getBRMC() {
        return BRMC.get();
    }

    public SimpleStringProperty BRMCProperty() {
        return BRMC;
    }

    public String getGHRQ() {
        return GHRQ.get();
    }

    public SimpleStringProperty GHRQProperty() {
        return GHRQ;
    }

    public String getHZLX() {
        return HZLX.get();
    }

    public SimpleStringProperty HZLXProperty() {
        return HZLX;
    }


}
