# mutants
Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar
contra los X-Men.
Te ha contratado a ti para que desarrolles un proyecto que detecte si un
humano es mutante basándose en su secuencia de ADN.
Para eso te ha pedido crear un programa con un método o función con la siguiente firma (En
alguno de los siguiente lenguajes: Java / Golang / C-C++ / C# / Javascript (node) / Python):

boolean isMutant(String[] dna); // Ejemplo Java

En donde recibirás como parámetro un array de Strings que representan cada fila de una tabla
de (NxN) con la secuencia del ADN. Las letras de los Strings solo pueden ser: (A,T,C,G), las
cuales representan cada base nitrogenada del ADN.

No-Mutante
A T G C G A
C A G T G C
T T A T T T
A G A C G G
G C G T C A
T C A C T G

Mutante Mutante
A T G C G A
C A G T G C
T T A T G T
A G A A G G
C C C C T A
T C A C T G

 
Sabrás si un humano es mutante, si encuentras más de una secuencia de cuatro letras
iguales, de forma oblicua, horizontal o vertical.
Ejemplo (Caso mutante):
String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
En este caso el llamado a la función isMutant(dna) devuelve “true”.

Desafíos:
Programa (en cualquier lenguaje de programación) que cumpla con el método pedido por
Magneto.
Crea una API REST, crear el servicio “/mutant/” en donde se pueda detectar si un humano es
mutante enviando la secuencia de ADN mediante un HTTP POST con un Json el cual tenga el
siguiente formato:
POST → /mutant/
{
"dna":[
"ATGCGA",
"CAGTGC",
"TTATGT",
"AGAAGG",
"CCCCTA",
"TCACTG"
]

}
En caso de verificar un mutante, debería devolver un HTTP 200-OK, en caso contrario un
403-Forbidden
Incluí los tests que considere necesarios.

Opcional
Anexa una base de datos, la cual guarde los ADN’s verificados con la API.
Solo 1 registro por ADN.
Expone un servicio extra “/stats” que devuelva un Json con las estadísticas de las
verificaciones de ADN: {“count_mutant_dna”:40, “count_human_dna”:100: “ratio”:0.4}
Tené en cuenta que la API puede recibir fluctuaciones agresivas de tráfico.
Entregar:
● Código fuente en repositorio de github.
● Readme en github con las instrucciones para ejecutar la API.
● Cualquier duda en la interpretación del ejercicio tomá una decisión y documentala
en el readme.
