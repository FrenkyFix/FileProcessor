# FileProcessor
Данная программа записывает разные типы данных, поданные на вход в нескольких файлах, в соответствующие выходные файлы.
Инструкция по запуску:
javac -d out Main.java FileProcessor.java
jar cfm util.jar MANIFEST.MF -C out .
java -jar util.jar -s -a -p sample- in1.txt in2.txt //Пример запуска: вывод краткой статистики, запись результатов в существующие файлы с префиксом sample-. Данные будут взяты из файлов in1.txt, in2.txt.

Содержание файла MANIFEST.MF:
Main-Class: Main //следующая строка обязательно пустая!

Версия java: openjdk version "23.0.2" 2025-01-21
