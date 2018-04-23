package fi.stardex.boschdemo.persistance.orm;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "injectors")
public class Injector {
    @Id
    @Column(name = "injector_code", nullable = false, length = 45)
    private String injectorCode;

    @Column(name = "codetype")
    private Integer codetype;

    @Column(name = "k_coefficient")
    private Integer k_coefficient;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "injector")
    private List<InjectorTest> injectorTests;

    public String getInjectorCode() {
        return injectorCode;
    }

    public Integer getCodetype() {
        return codetype;
    }

    public Integer getK_coefficient() {
        return k_coefficient;
    }

    public List<InjectorTest> getInjectorTests() {
        return injectorTests;
    }

    @Override
    public String toString() {
        return injectorCode;
    }
}
