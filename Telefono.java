package junittest;

public class Telefono {
    private int id;
    private String telefono;
    private int personaId;

    public Telefono(int id, String telefono, int personaId) {
        this.id = id;
        this.telefono = telefono;
        this.personaId = personaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumero() { return telefono; }
    public void setNumero(String numero) { this.telefono = numero; }

    public int getPersonaId() { return personaId; }
    public void setPersonaId(int personaId) { this.personaId = personaId; }
}
