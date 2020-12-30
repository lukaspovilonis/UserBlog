package lt.userblog.entities;

import lombok.Data;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class ResponseElement {

    @XmlElement
    private String info;

    @XmlElement
    private String error;

}