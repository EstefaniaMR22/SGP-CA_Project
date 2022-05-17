package model.dao;

import assets.utils.Database;
import assets.utils.DateFormatter;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import model.dao.interfaces.IWorkplanDAO;
import model.domain.Action;
import model.domain.ActionState;
import model.domain.Goal;
import model.domain.GoalState;
import model.domain.Workplan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkplanDAO implements IWorkplanDAO {
    private final Database database;

    public WorkplanDAO() {
        this.database = new Database();
    }

    /***
     * Add a workplan to database
     * @param workplan the objecto to save in database
     * @param idAcademicGroup the id of academic group
     * @return int representing the id of workplan
     */
    @Override
    public Workplan addWorkPlan(Workplan workplan, String idAcademicGroup) throws SQLException {
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String lastInsert = "SELECT LAST_INSERT_ID()";
            String statement = "INSERT INTO PlanDeTrabajo(id_programa_cuerpo_academico, duracion_anios, fecha_inicio, fecha_final, objetivo_general, identificador) VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, idAcademicGroup);
            preparedStatement.setInt(2, workplan.getYearsDuration());
            preparedStatement.setDate(3, DateFormatter.convertUtilDateToSQLDate(workplan.getStartDate()));
            preparedStatement.setDate(4, DateFormatter.convertUtilDateToSQLDate(workplan.getEndDate()));
            preparedStatement.setString(5, workplan.getGeneralObjetive());
            preparedStatement.setString(6, workplan.getIdentificator());
            preparedStatement.execute();
            preparedStatement = conn.prepareStatement(lastInsert);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workplan.setId(resultSet.getInt(1));
            }
            if (workplan.getId() > 0) {
                for (Goal goal : workplan.getGoalList()) {
                    String goalStatement = "INSERT INTO Meta(descripcion, fecha_fin, identificador, estado, id_plan_trabajo) VALUES(?,?,?,?,?)";
                    preparedStatement = conn.prepareStatement(goalStatement);
                    preparedStatement.setString(1, goal.getDescription());
                    preparedStatement.setDate(2, DateFormatter.convertUtilDateToSQLDate(goal.getEndDate()));
                    preparedStatement.setString(3, goal.getIdentificator());
                    preparedStatement.setInt(4, 1);
                    preparedStatement.setInt(5, workplan.getId());
                    preparedStatement.execute();
                    preparedStatement = conn.prepareStatement(lastInsert);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        goal.setId(resultSet.getInt(1));
                    }
                    if (goal.getId() > 0) {
                        for (Action action : ((goal.getActions() == null) ? new ArrayList<Action>() : goal.getActions())) {
                            String actionStatement = "INSERT INTO Accion(id_miembro, descripcion, id_meta, estado) VALUES(?,?,?,?)";
                            preparedStatement = conn.prepareStatement(actionStatement);
                            preparedStatement.setInt(1, action.getResponsable().getId());
                            preparedStatement.setString(2, action.getDescription());
                            preparedStatement.setInt(3, goal.getId());
                            preparedStatement.setInt(4, 1);
                            preparedStatement.execute();
                            preparedStatement = conn.prepareStatement(lastInsert);
                            resultSet = preparedStatement.executeQuery();
                            if (resultSet.next()) {
                                action.setId(resultSet.getInt(1));
                            }
                            if (action.getId() > 0) {
                                for (String resource : action.getRecursos()) {
                                    String resourceStatement = "INSERT INTO RecursoAccion(recurso, id_accion) VALUES(?,?)";
                                    preparedStatement = conn.prepareStatement(resourceStatement);
                                    preparedStatement.setString(1, resource);
                                    preparedStatement.setInt(2, action.getId());
                                    preparedStatement.execute();
                                }
                            }
                        }
                    }
                }
            }
            conn.commit();
        }
        return workplan;
    }

    /***
     * Get all workplan of academic group.
     * @param academicGroupID the academic group to get all workplans
     * @return List containin all the workplans registered in database
     */
    @Override
    public List<Workplan> getAllWorkplan(String academicGroupID) throws SQLException {
        List<Workplan> list;
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM PlanDeTrabajo";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            list = new ArrayList<>();
            while (resultSet.next()) {
                Workplan workplan = new Workplan();
                workplan.setIdentificator(resultSet.getString("identificador"));
                workplan.setId(resultSet.getInt("id"));
                workplan.setYearsDuration(resultSet.getInt("duracion_anios"));
                workplan.setStartDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_inicio")));
                workplan.setEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_final")));
                workplan.setGeneralObjetive(resultSet.getString("objetivo_general"));
                list.add(workplan);
            }
        }
        return list;
    }

    /***
     * Get all details (actions and goals) of workplan of database.
     * @param id the id of the workplan to get details
     * @return Workplan with details (actions and goals)
     */
    @Override
    public Workplan getWorkplanDetails(int id) throws SQLException {
        Workplan workplan = null;
        try (Connection conn = database.getConnection()) {
            String statement = "SELECT * FROM PlanDeTrabajo WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                workplan = new Workplan();
                workplan.setId(resultSet.getInt("id"));
                workplan.setIdentificator(resultSet.getString("identificador"));
                workplan.setYearsDuration(resultSet.getInt("duracion_anios"));
                workplan.setStartDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_inicio")));
                workplan.setEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet.getDate("fecha_final")));
                workplan.setGeneralObjetive(resultSet.getString("objetivo_general"));
                String goalStatement = "SELECT * FROM Meta WHERE id_plan_trabajo = ?";
                PreparedStatement preparedStatement1 = conn.prepareStatement(goalStatement);
                preparedStatement1.setInt(1, workplan.getId());
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                List<Goal> goalList = new ArrayList<>();
                while (resultSet1.next()) {
                    Goal goal = new Goal();
                    goal.setState(getGoalState(resultSet1.getString("estado")));
                    goal.setId(resultSet1.getInt("id"));
                    goal.setIdentificator(resultSet1.getString("identificador"));
                    goal.setDescription(resultSet1.getString("descripcion"));
                    goal.setEndDate(DateFormatter.convertSQLDateToUtilDate(resultSet1.getDate("fecha_fin")));
                    List<Action> actions = new ArrayList<>();
                    String actionStatement = "SELECT * FROM Accion WHERE id_meta = ?";
                    PreparedStatement preparedStatement2 = conn.prepareStatement(actionStatement);
                    preparedStatement2.setInt(1, goal.getId());
                    ResultSet resultSet2 = preparedStatement2.executeQuery();
                    while (resultSet2.next()) {
                        Action action = new Action();
                        action.setDescription(resultSet2.getString("descripcion"));
                        action.setState(getActionState(resultSet2.getString("estado")));
                        action.setResponsable(new MemberDAO().getMember(resultSet2.getInt("id_miembro")));
                        action.setId(resultSet2.getInt("id"));
                        String resourcesStatement = "SELECT * FROM RecursoAccion WHERE id_accion = ?";
                        PreparedStatement preparedStatement3 = conn.prepareStatement(resourcesStatement);
                        preparedStatement3.setInt(1, action.getId());
                        ResultSet resultSet3 = preparedStatement3.executeQuery();
                        List<String> recursos = new ArrayList<>();
                        while (resultSet3.next()) {
                            recursos.add(resultSet3.getString("recurso"));
                        }
                        action.setRecursos(recursos);
                        actions.add(action);
                    }
                    goal.setActions(actions);
                    goalList.add(goal);
                }
                workplan.setGoalList(goalList);
            }
        }
        return workplan;
    }

    /***
     * Update a workplan including goals and actions.
     * @param workplan all the new workplan to update
     * @param idAcademicGroup the id of academic group
     * @return workplan object that contains all new id
     */
    @Override
    public Workplan updateWorkplan(Workplan workplan, String idAcademicGroup) throws SQLException {
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String lastInsert = "SELECT LAST_INSERT_ID()";
            String statement = "UPDATE PlanDeTrabajo SET duracion_anios = ?, fecha_inicio = ?, fecha_final = ?, objetivo_general = ?, identificador = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, workplan.getYearsDuration());
            preparedStatement.setDate(2, DateFormatter.convertUtilDateToSQLDate(workplan.getStartDate()));
            preparedStatement.setDate(3, DateFormatter.convertUtilDateToSQLDate(workplan.getEndDate()));
            preparedStatement.setString(4, workplan.getGeneralObjetive());
            preparedStatement.setString(5, workplan.getIdentificator());
            preparedStatement.setInt(6, workplan.getId());
            preparedStatement.executeUpdate();
            // Update GOAL State or Remove Goal (only list saved in database)
            Workplan WorkplanAux = getWorkplanDetails(workplan.getId());
            List<Goal> oldGoalList = (WorkplanAux.getGoalList() != null) ? WorkplanAux.getGoalList() : new ArrayList<>() ;
            oldGoalList.forEach(x -> {
                Goal isPresent = workplan.getGoalList().stream().filter(e -> e.getIdentificator().equals(x.getIdentificator())).findAny().orElse(null);
                if (isPresent != null) {
                    if (isPresent.getState() != x.getState()) {
                        try {
                            updateActivityState(x.getIdentificator(), x.getId(), isPresent.getState());
                        } catch (SQLException e) {
                            Logger.getLogger(WorkplanDAO.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    if(isPresent.getActions() != null ) {
                        isPresent.getActions().forEach(act -> {
                            try {
                                String deleteActions = "DELETE FROM Accion WHERE id = ?";
                                PreparedStatement preparedStatementl = conn.prepareStatement(deleteActions);
                                preparedStatementl.setInt(1, act.getId());
                                preparedStatementl.executeUpdate();
                                String actionStatement = "INSERT INTO Accion(id_miembro, descripcion, id_meta, estado) VALUES(?,?,?,?)";
                                preparedStatementl = conn.prepareStatement(actionStatement);
                                preparedStatementl.setInt(1, act.getResponsable().getId());
                                preparedStatementl.setString(2, act.getDescription());
                                preparedStatementl.setInt(3, x.getId());
                                preparedStatementl.setInt(4, 1);
                                preparedStatementl.execute();
                                preparedStatementl = conn.prepareStatement(lastInsert);
                                ResultSet resultSet = preparedStatementl.executeQuery();
                                if (resultSet.next()) {
                                    act.setId(resultSet.getInt(1));
                                }
                                if (act.getId() > 0) {
                                    for (String resource : act.getRecursos()) {
                                        String resourceStatement = "INSERT INTO RecursoAccion(recurso, id_accion) VALUES(?,?)";
                                        PreparedStatement preparedStatementrs = conn.prepareStatement(resourceStatement);
                                        preparedStatementrs.setString(1, resource);
                                        preparedStatementrs.setInt(2, act.getId());
                                        preparedStatementrs.execute();
                                    }
                                }
                            } catch (SQLException e) {
                                Logger.getLogger(WorkplanDAO.class.getName()).log(Level.SEVERE, null, e);
                            }
                        });
                    }
                } else {
                    try {
                        removeGoal(x.getId());
                    } catch (SQLException e) {
                        Logger.getLogger(WorkplanDAO.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            });

            workplan.getGoalList().forEach( x -> {
                if(oldGoalList.stream().noneMatch( e -> x.getIdentificator().equals(e.getIdentificator()))) {
                    String goalStatement = "INSERT INTO Meta(descripcion, fecha_fin, identificador, estado, id_plan_trabajo) VALUES(?,?,?,?,?)";
                    PreparedStatement preparedStatementg = null;
                    try {
                        preparedStatementg = conn.prepareStatement(goalStatement);
                        preparedStatementg.setString(1, x.getDescription());
                        preparedStatementg.setDate(2, DateFormatter.convertUtilDateToSQLDate(x.getEndDate()));
                        preparedStatementg.setString(3, x.getIdentificator());
                        preparedStatementg.setInt(4, 1);
                        preparedStatementg.setInt(5, workplan.getId());
                        preparedStatementg.execute();

                        preparedStatementg = conn.prepareStatement(lastInsert);
                        ResultSet resultSet = preparedStatementg.executeQuery();
                        if (resultSet.next()) {
                            x.setId(resultSet.getInt(1));
                        }
                        if (x.getId() > 0) {
                            for (Action action : ((x.getActions() == null) ? new ArrayList<Action>() : x.getActions())) {
                                String deleteActions = "DELETE FROM Accion WHERE id = ?";
                                PreparedStatement preparedStatementl = conn.prepareStatement(deleteActions);
                                preparedStatementl.setInt(1, action.getId());
                                preparedStatementl.executeUpdate();
                                String actionStatement = "INSERT INTO Accion(id_miembro, descripcion, id_meta, estado) VALUES(?,?,?,?)";
                                preparedStatementg = conn.prepareStatement(actionStatement);
                                preparedStatementg.setInt(1, action.getResponsable().getId());
                                preparedStatementg.setString(2, action.getDescription());
                                preparedStatementg.setInt(3, x.getId());
                                preparedStatementg.setInt(4, 1);
                                preparedStatementg.execute();
                                preparedStatementg = conn.prepareStatement(lastInsert);
                                resultSet = preparedStatementg.executeQuery();
                                if (resultSet.next()) {
                                    action.setId(resultSet.getInt(1));
                                }
                                if (action.getId() > 0) {
                                    for (String resource : action.getRecursos()) {
                                        String resourceStatement = "INSERT INTO RecursoAccion(recurso, id_accion) VALUES(?,?)";
                                        preparedStatementg = conn.prepareStatement(resourceStatement);
                                        preparedStatementg.setString(1, resource);
                                        preparedStatementg.setInt(2, action.getId());
                                        preparedStatementg.execute();
                                    }
                                }
                            }
                        }

                    } catch (SQLException e) {
                        Logger.getLogger(WorkplanDAO.class.getName()).log(Level.SEVERE, null, e);
                    }

                }
            });
            conn.commit();
        }
        return workplan;
    }

    /***
     * Remove a workplan from database.
     * @param id the workplan id
     * @return true if was removed otherwise false
     */
    @Override
    public boolean removeWorkplan(int id) throws SQLException {
        return false;
    }

    public boolean removeGoal(int idGoal) throws SQLException {
        boolean isRemoved = false;
        try (Connection conn = database.getConnection()) {
            String statement = "DELETE FROM Meta WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, idGoal);
            int rowsAffected = preparedStatement.executeUpdate();
            isRemoved = rowsAffected > 0;
        }
        return isRemoved;
    }

    public boolean updateActivityState(String identificator, int goalID, GoalState state) throws SQLException {
        boolean isUpdated = false;
        try (Connection conn = database.getConnection()) {
            conn.setAutoCommit(false);
            String statement = "UPDATE Meta SET estado = ? WHERE identificador = ? AND id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, (state == GoalState.ACTIVE ? 1 : 2));
            preparedStatement.setString(2, identificator);
            preparedStatement.setInt(3, goalID);
            isUpdated = preparedStatement.executeUpdate() > 0;
            conn.commit();
        }
        return isUpdated;
    }

    private GoalState getGoalState(String type) {
        for (GoalState goalState : GoalState.values()) {
            if (type.equalsIgnoreCase(goalState.getActivityState())) {
                return goalState;
            }
        }
        return null;
    }

    private ActionState getActionState(String type) {
        for (ActionState actionState : ActionState.values()) {
            if (type.equalsIgnoreCase(actionState.getActionState())) {
                return actionState;
            }
        }
        return null;
    }

}
