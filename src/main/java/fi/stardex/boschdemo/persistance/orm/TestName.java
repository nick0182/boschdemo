package fi.stardex.boschdemo.persistance.orm;

import javax.persistence.*;

@Entity
@Table(name = "test_name")
public class TestName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "test_name", unique = true, nullable = false, length = 45)
    private String testName;


    @Column(name = "display_order", unique = true, nullable = false)
    private Integer displayOrder;

    public Integer getId() {
        return id;
    }

    public String getTestName() {
        return testName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    @Override
    public String toString() {
        return testName;
    }
}
