package pe.edu.upeu.ventafx.modelo;

    public class Usuario {
        private int id;
        private String nombreUsuario;
        private String contrasena;

        // Constructor
        public Usuario(int id, String nombreUsuario, String contrasena) {
            this.id = id;
            this.nombreUsuario = nombreUsuario;
            this.contrasena = contrasena;
        }

        // Getters y Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public void setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }

