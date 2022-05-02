'''
Tipado de datos (dinámico y estático)
Constantes
Alcance local
Alcance anidado
Alcance global
'''
#  +--------------------------------------------------------------------------------------+
#  | DESCRIPCION: Tipos de daots, alcance y variables                                     |
#  | ESTUDIANTE: Juan Diego Collazos Perafan - collazosdie@unicauca.edu.co                | 
#  |             Yenny Eliana Cerquera Yacumal – yecery@unicauca.edu.co                   |
#  | FECHA: 2022/01/19                                                                    |
#  +--------------------------------------------------------------------------------------+

#  +--------------------------------------------------------------------------------------+
#  | ARCHIVOS DE CABECERA Y LIBRERIAS UTILIZADAS                                          |
#  +--------------------------------------------------------------------------------------+
##### Las palabras que van de la línea 22 a la 30 (exceptuando los comentarios, y la palabra "pd") 
##### son palabras reservadas, como "import", "as", y las correspondientes a los nombres de los módulos:

# Módulo para manipular archivos csv
import csv
# Módulo de herramienta para manipular y analizar datos
import pandas as pd
# Módulo para recorrer tags html
from bs4 import BeautifulSoup
# Módulo para importar un webdriver de selenium y manipular navegadores
from selenium import webdriver
# Módulo para trabajar con Chrome
from webdriver_manager.chrome import ChromeDriverManager

#  +--------------------------------------------------------------------------------------+
#  | Constantes                                                                           |
#  +--------------------------------------------------------------------------------------+
# Indica si los resultados de la búsqueda se deben filtrar buscando que contentan los 
# términos de búsqueda 
EXPLICIT_SEARCH = True # es una variable de alcance global (variable global)

#  +--------------------------------------------------------------------------------------+
#  |                                FUNCIÓN get_url                                       |
#  | Genera la url para navegar entre las diferentes páginas de un dominio (amazon)       |
#  +--------------------------------------------------------------------------------------+
def get_url (search_term): # def es la palabra reservada que hace referencia que se usará una función o método
    # Plantilla para la búsqueda en amazon con el placeholder para el término a buscar
    template = "https://www.amazon.com/s?k={}&" # variable local
    # En la url no pueden ir espacios, se reemplaza por un '+'
    search_term = search_term.replace(" ", "+")
    
    # Se agrega el término de búsqueda al placeholder de la plantilla
    url = template.format(search_term) # variable local
    
    # Se agrega el placeholder para las diferentes páginas, además del lenguaje en que se cargará la página
    url += "page={}&language=es_US"
    
    return url # palabra reservada que permite el retorno de un dato a partir de lo que se haga en la función

#  +--------------------------------------------------------------------------------------+
#  |                                FUNCIÓN search_helper                                 |
#  | Recibe la descripción de un ítem de las búsquedas y el término de búsqueda, comprueba|
#  | si el término de búsqueda se encuentra en la descripción del item                    |
#  +--------------------------------------------------------------------------------------+
def search_helper(description, search_term):
    # Comprueba si es una sola palabra como término de búsqueda
    if len(search_term.split(" ")) < 2: # "if" es la palabra reservada para condicionales
        search_term += " " + search_term # Se agrega la misma palabra separada por espacio, así el for no toma cada carácter
                                         # ya que eso haría que cualquier item de amazon con una letra compatible ya retorne como resultado
                                         # variable local

    # Recorre cada palabra en el término de búsqueda 
    for each_term in search_term: # "for" es la palabra reservada para hacer iteraciones, con la ayuda de "in" que sirve para saber si un elemento
                                  # se encuentra en una clase iterable  
                                  # "each_term" variable local
        # Retorna verdadero si en la descripción se encuentra una de las palabras
        if description.lower().find(each_term.lower()) != -1:
            return True # true y false son palabras reservadas, que representan a los tipos booleanos
    return False 

#  +--------------------------------------------------------------------------------------+
#  |                                FUNCIÓN extract_record                                |
#  | Recibe un item de cada publicacion gracias a beautifulsoup, explora la información   |
#  | del item y genera un registro que guarda los datos de nuestro interés del item       |
#  +--------------------------------------------------------------------------------------+
def extract_record (item, search_term):
    # Cada navegación al dato varía según la estructura de la página origen

    # Ubicación de la descripcion y url
    atag = item.h2.a # variable local
    
    # Extrae el texto del item, es decir la descripción
    description = atag.text.strip() # variable local

    # Si el modo de búsqueda explícita es verdadero, entonces hay que ver si alguna de las palabras
    # en la descripción concuerdan con el término de búsqueda
    if EXPLICIT_SEARCH and (not search_helper(description.lower(), search_term.lower())): #se invoca a la variable de alcance global
        # Retorna vacío si la búsqueda es explícita, y además ningún término en la descripción concuerda
        return

    # Define el dominio de la página, y agrega el hiperlink a la dirección de la publicación
    url = "https://www.amazon.com" + atag.get("href") # variable local. Ya se había asignado este mismo nombre a otra variable en otra función, pero no tiene que ver con esta
                                                      # es decir, no se modifican entre sí porque no están en el mismo bloque
    
    try: # "try" y "except" son palabras reservadas para las excepciones, que permiten saber qué hacer cuando algo no funciona como se espera
        # Busca el tag padre de precio en el item, esto por la estructura de la página
        price_parent = item.find("span", "a-price")

        # Busca el precio y lo extrae como texto
        price = price_parent.find("span", "a-price-whole").text #tipado de datos estático, porque es necesario que se extraiga como texto para no tener problemas después
    except AttributeError: # "AttributeError" es una palabra reservada que hace referencia a un error que se genera cuando falla una referencia o asignación de un atributo
        # Si no existe un precio para la publicación, no nos interesa este item
        return
    
    try:
        # Extrae el texto en la ubicación del rating para el item
        rating = item.i.text
        # Busca el conteo de reseñas y extrae el texto
        review_count = item.find("span", {"class": "a-size-base"}).text #tipado de datos estático, porque es necesario que se estraiga como texto para no tener problemas después
    except AttributeError:
        # Si no se encuentran aún podemos usar el item, se dejan string vacías
        rating = "" #tipado de datos estático, porque es necesario que se mantenga el tipo de dato de string
        review_count = "" #lo mismo que en la anterior variable
    
    # Tupla con la información que nos interesa
    result = (description, price.replace(".",""), rating, review_count, url)
    
    return result

#  +--------------------------------------------------------------------------------------+
#  |                                FUNCIÓN scrap_Data                                    |
#  | Recibe un término de búsqueda, se encarga de iniciar el controlador del navegador,   |
#  | itera cada página de la búsqueda de ese término, para cada iteración guarda un       |
#  | objeto de beautifulsoup, recibe los registros útiles y los agrega a una lista,       |
#  | retorna la lista de registros                                                        |
#  +--------------------------------------------------------------------------------------+
def scrap_Data (search_term):
    # Inicia el controlador del navegador
    driver = webdriver.Chrome(ChromeDriverManager().install())
    
    # Declaramos una lista con los registros
    records = []

    # Obtenemos la url para el término de búsqueda indicado
    url = get_url(search_term)
    
    # Recorrido para las 20 páginas de una búsqueda en amazon
    for page in range(1, 2):
        # Indica la url a entrar en el navegador
        driver.get(url.format(page))
        # Recibe la información de la página en un objeto bs4
        soup = BeautifulSoup(driver.page_source, "html.parser")
        # Almacena cada publicación en la página de búsqueda
        results = soup.find_all("div", {"data-component-type": "s-search-result"})
        
        # Recorre cada resultado de la página
        for item in results:
            # Recibe el registro de los datos del item
            record = extract_record(item, search_term)
            # Si existe se agrega a los registros locales
            if record:
                records.append(record)

    # Cierra el controlador del navegador
    driver.close()

    # Retorna los registros
    return records

#  +--------------------------------------------------------------------------------------+
#  |                          Procedimiento write_Data                                    |
#  | Recibe los registros, con un gestor de documento escribe los nombres de las columnas |
#  | y todos los registros                                                                |
#  +--------------------------------------------------------------------------------------+
def write_Data(records):
    # Abre el gestor de documento
    with open("results.csv", "w", newline = "", encoding = "utf-8") as f:
        # Escritor de csv
        writer = csv.writer(f) #variable de alcance local
        # Escribe los nombres de las columnas
        writer.writerow(["Description", "Price (USD)", "Rating", "ReviewCount", "Url"])
        # Escribe los datos
        writer.writerows(records)

#  +--------------------------------------------------------------------------------------+
#  |                          Procedimiento read_Data                                     |
#  | Lee el archivo csv en la ruta indicada y lo carga a un dataframe                     |
#  +--------------------------------------------------------------------------------------+
def read_Data(ruta):
    df = pd.read_csv("results.csv")
    return df

#  +--------------------------------------------------------------------------------------+
#  |                          Procedimiento show_Data                                     |
#  | Muestra los datos recopilados en el dataFrame                                        |
#  +--------------------------------------------------------------------------------------+
def show_Data():
    print("\nDatos recopilados:")
    # Imprime el dataFrame
    print(df)

#  +--------------------------------------------------------------------------------------+
#  |                                Funcion get_Menu                                      |
#  | retorna el menu                                                                      |
#  +--------------------------------------------------------------------------------------+
def get_Menu():
    return  ("\n:::::::::MENU:::::::::" +
            "\n: 1. Mejores precios :"+
            "\n: 2. Mejor valorados :"+
            "\n: 3. Mostrar todos   :"+
            "\n: 0. Salir           :"+
            "\n::::::::::::::::::::::\n")


#  +--------------------------------------------------------------------------------------+
#  |                                Clase funcionalidades                                 |
#  | Agrupa las funciones que sirven para generar la lista de los productos con los       |
#  | mejores precios y los mejor valorados.                                               |
#  +--------------------------------------------------------------------------------------+
class funcionalidades: # Aquí se aplica el alcance anidado, pues esta clase tiene dos funciones
    #  +--------------------------------------------------------------------------------------+
    #  |                                FUNCIÓN mejores_Precios                               |
    #  | Muestra los n mejores precios a través de manejo de los datos con el data frame      |
    #  +--------------------------------------------------------------------------------------+
    def mejores_Precios(self, n):
        print("\nMejores precios obtenidos: ")
        # datos en los que la columna de precios sea menor que el promedio de la misma columna
        # el dataframe es ordenado según la columna de precios
        bestPrices = df[df["Price (USD)"] < df["Price (USD)"].mean()].sort_values("Price (USD)")
    
        # Imprime las primeras n filas del Dataframe
        print(bestPrices.head(n))

    #  +--------------------------------------------------------------------------------------+
    #  |                                FUNCIÓN mejor_Valorados                               |
    #  | Muestra las n mejores valoraciones a través de manejo de los datos con el data frame |
    #  +--------------------------------------------------------------------------------------+
    def mejor_Valorados(self, n):
        print("\nMejores valoraciones obtenidas: ")
        # Ordena los datos según la columna Rating
        bestRated = df.sort_values("Rating", ascending=False)

        # Muestra los primeros n datos donde la columna Rating no sea nula
        print(bestRated[bestRated["Rating"].notna()].head(n))

#  +--------------------------------------------------------------------------------------+
#  |                                Procedimiento funciones                               |
#  | Pide un input cuando muestra el menu, llama a la función correspondiente según esto  |
#  +--------------------------------------------------------------------------------------+
def funciones () :
    funcion = funcionalidades()
    while (True): # "while", palabra reservada para los bucles
        opcion = input(get_Menu())
        n = 15
        if opcion == "1":
            funcion.mejores_Precios(n)
        if opcion == "2":
            funcion.mejor_Valorados(n)
        if opcion == "3":
            show_Data()
        if opcion == "0":
            return

#  +--------------------------------------------------------------------------------------+
#  |                                Procedimiento funciones                               |
#  | Pregunta si se quiere desactivar la búsqueda explícita, y pide el término para la    |
#  | búsqueda                                                                             |
#  +--------------------------------------------------------------------------------------+
def configurar_Programa():
    # Variable global
    global EXPLICIT_SEARCH
    b = input("Desactivar busqueda explicita? [1] Si: ")
    if b == "1":
        EXPLICIT_SEARCH = False
    # Pide y retorna los términos a buscar
    return input("Ingrese el/los términos de búsqueda: ")
    

# Configura el programa, pide la búsqueda, realiza la búsqueda y escribe los datos
write_Data(scrap_Data(configurar_Programa()))

# Carga los datos a un data frame
df = read_Data("results.csv")

# Llama a ejecutar las funcionalidades con los datos
funciones()