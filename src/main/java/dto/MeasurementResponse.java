package dto;

import java.util.List;

public class MeasurementResponse {
    List<MeasurementsDTO> list;

    public List<MeasurementsDTO> getList() {
        return list;
    }

    public void setList(List<MeasurementsDTO> list) {
        this.list = list;
    }
}
