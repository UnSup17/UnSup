import csv
import os

def getFormattedLines (tabla, datatypes, row):
    line = "insert into " + tabla + " values ("
    i = 0
    for dtype in datatypes:
        if dtype == "number":
            line += row[i] + ","
        if dtype == "string":
            line += "'" + row[i] + "',"
        if dtype == "date":
            line += "TO_DATE('" + row[i] + "','DD-MON-YYYY'),"
        i += 1
    line = line[:-1]
    line += ");\n"
    return line

with open('BD.txt') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    line_count = 0
    tabla = ""
    datos = ""
    file = open(".\scriptInsertValues.txt", "w")
    for row in csv_reader:
        if row[0] != "":
            if line_count == 0:
                tabla = row[0]
            if line_count == 2:
                datatypes = row
            if line_count > 2:
                datos += getFormattedLines(tabla, datatypes, row)
            line_count += 1
            if line_count == 13:
                line_count = 0
        else:
            line_count = 0
    file.write(datos)
    file.close()
