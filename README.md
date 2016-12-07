# AutoDrone

## Grupo

Lucas Chan Tcheou
Kenzo Tomida
Paula Ueda
Gabriel Casarim

## Estrutura do Projeto

.. AutoDrone/app/src/main/java/testing/gps_service

    # GPS_Service - Serviço que escuta a posição atual do celular. À qualquer mudança de posição, manda um broadcast com as novas coordenadas (latitude e longitude)
    # MainActivity.java - Classe main do aplicativo. Fica à espera de um sinal de broadcast, na função onResume() com novas posições de GPS. Ao recebê-las, trata as coordenadas e calcula os novos comandos a serem enviados ao Drone
    
.. Rhino_Java.txt e Rhino_JS.txt

    # Seriam usados para a conversão de Java para JavaScript, mas não chegou a ser implementado