package fi.stardex.boschdemo.persistance.orm;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "injector_test")
public class InjectorTest implements Comparable<InjectorTest> {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "injector_code")
    private Injector injector;

    @ManyToOne
    @JoinColumn(name = "test_name")
    private TestName testName;

    @Column(name = "motor_speed")
    private Integer motorSpeed;

    @Column(name = "setted_pressure")
    private Integer settedPressure;

    @Column(name = "adjusting_time")
    private Integer adjustingTime;

    @Column(name = "measurement_time")
    private Integer measurementTime;

    @Column(name = "codefield", length = 45)
    private String codefield;

    @Column(name = "injection_rate")
    private BigDecimal injectionRate;

    @Column(name = "total_pulse_time")
    private BigDecimal totalPulseTime;

    @Column(name = "coding_flow_range")
    private BigDecimal codingFlowRange;

    @Column(name = "nominal_flow")
    private BigDecimal nominalFlow;

    @Column(name = "flow_range")
    private BigDecimal flowRange;

    @Column(name = "max_correction")
    private Integer maxCorrection;

    @Column(name = "responce_time")
    private BigDecimal responceTime;

    public BigDecimal getNominalFlow() {
        return nominalFlow;
    }

    public BigDecimal getFlowRange() {
        return flowRange;
    }

    public TestName getTestName() {
        return testName;
    }

    @Override
    public int compareTo(InjectorTest o) {
        return testName.getDisplayOrder() - o.testName.getDisplayOrder();
    }

    @Override
    public String toString() {
        return testName.getTestName();
    }
}
