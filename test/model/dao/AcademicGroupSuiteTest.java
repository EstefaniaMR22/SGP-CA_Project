package model.dao;

import model.dao.academicgroupdao.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value = Suite.class)
@Suite.SuiteClasses({
        AddAcademicGroupTest.class,
        GetAllAcademicGroupTest.class,
        GetAcademicGroupDetailsTest.class,
        GetConsolidationGradeTest.class,
        UpdateAcademicGroupTest.class,
        RemoveAcademicGroupTest.class,
})


public class AcademicGroupSuiteTest {
}
