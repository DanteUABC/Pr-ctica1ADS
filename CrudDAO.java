package junittest;

import javafx.collections.ObservableList;

public interface CrudDAO<T> {
    
    ObservableList<T> obtenerTodos();
    void insertar(T objeto);
    void actualizar(T objeto);
    void eliminar(int id);
    
}
