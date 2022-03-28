package model.dao;

import model.dao.academicgroupdao.AddAcademicGroupTest;
import model.dao.academicgroupdao.GetAllAcademicGroupTest;
import model.dao.academicgroupdao.GetConsolidationGradeTest;
import model.dao.academicgroupdao.RemoveAcademicGroupTest;
import model.dao.academicgroupdao.UpdateAcademicGroupTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value = Suite.class)
@Suite.SuiteClasses({
        AddAcademicGroupTest.class,
        GetAllAcademicGroupTest.class,
        GetConsolidationGradeTest.class,
        UpdateAcademicGroupTest.class,
        RemoveAcademicGroupTest.class,
})


public class AcademicGroupSuiteTest {
}
