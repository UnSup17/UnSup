from datetime import datetime
from dateutil.relativedelta import relativedelta

estudio = True
inicioEstudio = datetime.now()
print("Estudia")
sec = 0

while (1):
    finEstudio = relativedelta(inicioEstudio, datetime.now())
    if finEstudio.minutes == -25:
        inicioDescanso = datetime.now()
        print("\n\nDescansa\n\n")
        bandera = True
        while (bandera):
            finDescanso = relativedelta(inicioDescanso, datetime.now())
            if finDescanso.minutes == -5:
                print("Estudia")
                inicioEstudio = datetime.now()
                bandera = False