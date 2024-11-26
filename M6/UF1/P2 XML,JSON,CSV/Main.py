import os
import pandas as pd
import xml.etree.ElementTree as ET
import json

def read_csv(nombre_archivo):
    try:
        df = pd.read_csv(nombre_archivo)
        return df
    except FileNotFoundError:
        print(f"El archivo {nombre_archivo} no fue encontrado.")
    except pd.errors.EmptyDataError:
        print("El archivo está vacío.")
    except pd.errors.ParserError:
        print("Error al procesar el archivo.")
    except Exception as e:
        print(f"Ocurrió un error inesperado: {e}")


def write_csv(df, document_name):

    directorio = os.path.dirname(os.path.abspath(__file__))
    resumen_csv_path = os.path.join(directorio, document_name)

    try:
        df.to_csv(resumen_csv_path, index=False, encoding='utf-8')
        print(f"\n Archivo '{resumen_csv_path}' GUARDADO CORRECTAMENTE.")
    except PermissionError:
        print(f"Error: No se puede guardar el archivo '{resumen_csv_path}'. "
              "Por favor, asegúrate de que no esté abierto en otro programa y vuelve a intentarlo.")
    except Exception as e:
        print(f"Ocurrió un error inesperado: {e}")
def read_xml(nombre_archivo):
    try:
        tree = ET.parse(nombre_archivo)
        root = tree.getroot()
        datos = []
        for elemento in root.findall('producto'):
            id_producto = int(elemento.find('ID').text)
            nombre_producto = elemento.find('Nombre').text
            categoria_producto = elemento.find('Categoría').text

            producto_info = {
                'ID': id_producto,
                'Nombre': nombre_producto,
                'Categoría': categoria_producto,
            }
            datos.append(producto_info)
        return datos
    except FileNotFoundError:
        print(f"El archivo {nombre_archivo} no fue encontrado.")
        return None
    except ET.ParseError:
        print("Error al procesar el archivo XML.")
        return None
    except Exception as e:
        print(f"Ocurrió un error inesperado: {e}")
        return None

def read_json(nombre_archivo):
    try:
        with open(nombre_archivo, 'r', encoding='utf-8') as archivo:
            datos = json.load(archivo)
            return datos
    except FileNotFoundError:
        print(f"El archivo {nombre_archivo} no fue encontrado.")
    except json.JSONDecodeError:
        print("Error al decodificar el archivo JSON.")
    except Exception as e:
        print(f"Ocurrió un error inesperado: {e}")

def opcion1():
    archivo_csv = 'ventas.csv'
    ventas_df = read_csv(archivo_csv)
    if ventas_df is not None:
        print("1.Calcula el total de ventas para cada producto (multiplica Cantidad por Precio_Unitario).")
        ventas_df['Total_Venta'] = ventas_df['Cantidad'] * ventas_df['Precio_Unitario']
        print(ventas_df)

        print('\n 2.	Agrupa los resultados por Producto y muestra el total de ventas por cada uno.')
        total = ventas_df.groupby('Producto')['Total_Venta'].sum()
        print(total)

        print('\n 3.	Filtra las ventas que ocurrieron en el primer trimestre del año (enero, febrero, marzo).')
        ventas_df['Fecha'] = pd.to_datetime(ventas_df['Fecha'])
        trimestre = ventas_df[(ventas_df['Fecha'].dt.month >= 1) & (ventas_df['Fecha'].dt.month <= 3)]
        print(trimestre)

def opcion2():
    archivo_xml = 'productos.xml'
    datos_productos = read_xml(archivo_xml)

    if datos_productos is not None:
        print('1.	Extrae los nombres de los productos que pertenecen a la categoría "Electrónica".')
        categoria = 'Electrónica'
        filtrado_categoria = []
        for producto in datos_productos:
            if producto['Categoría'] == categoria:
                filtrado_categoria.append(producto)
        for producto in filtrado_categoria:
            print(f"ID: {producto['ID']}, Nombre: {producto['Nombre']}, Categoría: {producto['Categoría']}")

        print("\n 2.	Crea un diccionario donde las claves sean los ID de producto y los valores sean los nombres de los productos.")
        diccionario_productos = {producto['ID']: producto['Nombre'] for producto in datos_productos}
        print(diccionario_productos)

        print("\n 3.	Encuentra la cantidad total de productos por categoría.")
        liscado_categorias = [producto['Categoría'] for producto in datos_productos]
        for categoria in set(liscado_categorias):  # set() para obtener categorías únicas
            cantidad = liscado_categorias.count(categoria)
            print(f"{categoria}: {cantidad}")

def opcion3():
    archivo_json = 'clientes.json'
    clientes = read_json(archivo_json)
    if clientes is not None:
        print('1. Extrae los correos electrónicos de todos los clientes que viven en la ciudad "Madrid".')
        ciudad = 'Madrid'
        filtro_correos_por_ciudad = [cliente['Email'] for cliente in clientes if cliente['Ciudad'] == ciudad]
        if filtro_correos_por_ciudad:
            for filtrado in filtro_correos_por_ciudad:
                print(filtrado)
        else:
            print("No se encontraron clientes en " + ciudad + ".")

        print('\n2. Calcula cuántos clientes únicos hay en cada ciudad.')
        cantidad_por_ciudad = {}

        for cliente in clientes:
            ciudad = cliente['Ciudad']
            if ciudad in cantidad_por_ciudad:
                cantidad_por_ciudad[ciudad] += 1
            else:
                cantidad_por_ciudad[ciudad] = 1
        for ciudad, cantidad in cantidad_por_ciudad.items():
            print(f"Ciudad: {ciudad}, Clientes: {cantidad}")

        print('\n3. Añade un nuevo campo "VIP" a cada cliente basado en la ciudad "Barcelona".')
        for cliente in clientes:
            cliente['VIP'] = True if cliente['Ciudad'] == 'Barcelona' else False
        for cliente in clientes:
            print(f"ID: {cliente['ID_Cliente']}, Nombre: {cliente['Nombre']}, Ciudad: {cliente['Ciudad']}, VIP: {cliente['VIP']}")

def opcion4():
    archivo_xml = 'productos.xml'
    archivo_json = 'clientes.json'
    archivo_csv = 'ventas.csv'
    datos_productos = read_xml(archivo_xml)
    clientes = read_json(archivo_json)
    ventas_df = read_csv(archivo_csv)
    #1..................................................................................................................
    print("1.¿Cuál es el total de ventas por cliente (utilizando los ID de ventas.csv y clientes.json)?")
    ventas_df['Total_Venta'] = ventas_df['Cantidad'] * ventas_df['Precio_Unitario']
    total_por_cliente = {cliente['ID_Cliente']: 0 for cliente in clientes}

    for index, row in ventas_df.iterrows():
        id_venta = row['ID_Venta']
        if id_venta <= len(total_por_cliente):
            total_por_cliente[id_venta] += row['Total_Venta']

    print("\nTotal de ventas por cliente:")
    for cliente in clientes:
        id_cliente = cliente['ID_Cliente']
        total_venta = total_por_cliente.get(id_cliente, 0)
        print(f"Cliente: {cliente['Nombre']}, Total de Ventas: {total_venta}")
    #2..................................................................................................................
    print("\n 2.¿Cuáles son los productos más vendidos por categoría?")
    datos_productos = pd.DataFrame(datos_productos)
    total_por_producto = ventas_df.groupby('Producto')['Cantidad'].sum().reset_index()
    merged_df = total_por_producto.merge(datos_productos, left_on='Producto', right_on='ID', how='left')
    ventas_por_categoria = merged_df.groupby('Categoría').agg({'Cantidad': 'sum'}).reset_index()

    print("\nProductos más vendidos por categoría:")
    for index, row in ventas_por_categoria.iterrows():
        print(f"Categoría: {row['Categoría']}")
        print(f"  Total Vendido: {row['Cantidad']}")

    #3..................................................................................................................
    print("\n3.¿Qué porcentaje de las ventas totales corresponde a clientes que viven en ciudades donde la categoría 'Electrónica' es popular (25%)?")
    clientes_df = pd.DataFrame(clientes)
    ids_productos_electronica = datos_productos[datos_productos['Categoría'] == 'Electrónica']['ID']
    ventas_electronica = ventas_df[ventas_df['Producto'].isin(ids_productos_electronica)].copy()
    ventas_electronica['Total_Venta'] = ventas_electronica['Cantidad'] * ventas_electronica['Precio_Unitario']
    total_ventas = ventas_electronica['Total_Venta'].sum()

    ventas_por_ciudad = (
        ventas_electronica.merge(clientes_df, left_on='ID_Venta', right_on='ID_Cliente')
        .groupby('Ciudad')['Total_Venta']
        .sum()
        .reset_index()
    )

    ventas_por_ciudad['Porcentaje'] = (ventas_por_ciudad['Total_Venta'] / total_ventas * 100) if total_ventas > 0 else 0
    ciudades_filtradas = ventas_por_ciudad[ventas_por_ciudad['Porcentaje'] > 25]
    print("Porcentajes de ventas por ciudad:")
    total_porcentaje = ciudades_filtradas['Porcentaje'].sum()
    for index, row in ciudades_filtradas.iterrows():
        print(f"{row['Ciudad']}: {row['Porcentaje']:.2f}%")
    print("-" * 30)
    print(f"Total: {total_porcentaje:.2f}%")


def opcion5():
    archivo_xml = 'productos.xml'
    archivo_json = 'clientes.json'
    archivo_csv = 'ventas.csv'
    archivo_save_csv = 'resumen_ventas.csv'
    datos_productos = read_xml(archivo_xml)
    clientes = read_json(archivo_json)
    ventas_df = read_csv(archivo_csv)

    print("\n Exporta los resultados obtenidos en los pasos anteriores a un nuevo archivo CSV llamado resumen_ventas.csv, que debe incluir:")

    if isinstance(datos_productos, list):
        datos_productos = pd.DataFrame(datos_productos)

    clientes_df = pd.DataFrame(clientes)
    ventas_df['Total_Venta'] = ventas_df['Cantidad'] * ventas_df['Precio_Unitario']

    resumen_df = (
        ventas_df.merge(clientes_df, left_on='ID_Venta', right_on='ID_Cliente', how='left')
        .merge(datos_productos, left_on='Producto', right_on='ID', how='left')
    )

    resumen_df = resumen_df.groupby(['Nombre_x', 'Ciudad', 'Nombre_y']).agg({'Total_Venta': 'sum'}).reset_index()
    resumen_df.rename(columns={'Nombre_x': 'Nombre', 'Nombre_y': 'Producto'}, inplace=True)

    write_csv(resumen_df, archivo_save_csv)

def salir():
    print("Saliendo del programa...hasta luegi")
    exit()

def menu_principal():
    opciones = {
        '1': ('Procesamiento de Datos CSV', opcion1),
        '2': ('Procesamiento de Datos XML', opcion2),
        '3': ('Procesamiento de Datos JSON', opcion3),
        '4': ('Integración y Análisis Completo', opcion4),
        '5': ('Exportación de Resultados', opcion5),
        '0': ('Salir', salir)
    }

    opcion = None
    while opcion != '0':
        print("\n--- Menú Principal ---")
        for clave in sorted(opciones):
            print(f' {clave}.- {opciones[clave][0]}')

        opcion = input('Seleccione una opción: ')
        if opcion in opciones:
            opciones[opcion][1]()
        else:
            print("Opción incorrecta, vuelva a intentarlo.")
        print()

if __name__ == '__main__':
    menu_principal()