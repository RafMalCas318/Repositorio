#include <iostream>
#include <cstdlib>  // Para getenv()
using namespace std;

extern "C" {
    bool primo(int n);
}

int main() {
    // Cabecera HTTP obligatoria para CGI
    cout << "Content-type: text/html\n\n";
    
    // HTML básico con estilos
    cout << R"(
    <!DOCTYPE html>
    <html>
    <head>
        <title>Verificador de Primos</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f0f0f0;
                margin: 0;
                padding: 20px;
            }
            .container {
                max-width: 600px;
                margin: 0 auto;
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            h1 {
                color: #4682B4; /* Azul */
                text-align: center;
            }
            form {
                margin: 20px 0;
                text-align: center;
            }
            input[type="number"] {
                padding: 8px;
                width: 100px;
            }
            button {
                background-color: #FFD700; /* Amarillo */
                border: none;
                padding: 8px 15px;
                border-radius: 4px;
                cursor: pointer;
            }
            .result {
                margin-top: 20px;
                padding: 10px;
                text-align: center;
                font-size: 18px;
            }
            .primo {
                color: #FFD700; /* Amarillo */
                font-weight: bold;
            }
            .no-primo {
                color: #4682B4; /* Azul */
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Verificador de N&uacute;meros Primos</h1>
            <form method="GET" action="">
                <input type="number" name="numero" required placeholder="Ingrese un n&uacute;mero">
                <button type="submit">Comprobar</button>
            </form>
    )";

    // Procesar parámetro de la URL
    const char* query = getenv("QUERY_STRING");
    if(query && string(query).find("numero=") != string::npos) {
        int num = atoi(query + 7); // Extrae el número después de "numero="
        
        if(num > 0) {
            bool esPrimo = primo(num);
            
            cout << "<div class='result " << (esPrimo ? "primo" : "no-primo") << "'>"
                 << "El n&uacute;mero " << num 
                 << (esPrimo ? " ES primo" : " NO es primo")
                 << "</div>";
        }
    }

    cout << R"(
        </div>
    </body>
    </html>
    )";

    return 0;
}