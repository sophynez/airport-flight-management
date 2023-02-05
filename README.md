 ### Flights management in an airport using one server and multiple clients architecture using sockets in Java.
 <br>

- Server : control tower
- clients : Airplanes

* An airplane is assigned to a flight
* The server handles and treats the requests from multiple clients

### Control tower:
- Registration flights
- Treats sates of airplane : 
    - actif
    - standby
    - idle
    - broken 
- Airplane routing
    - status and posiiton of the planes in real time
    - radar display
    - use shortest path algorithm for planes rajectory
    

    
    ![2](https://user-images.githubusercontent.com/61004176/216826297-b71b0169-f30f-41d0-806b-88706d9db22f.png)

## Radar display of flights 
States of an airplane :
- Red : broken (crash)
- Yellow : in danger either because of lack of fuel or potential collision with other plane
- Green : actif and safe

<div align="center">
    <img src="https://user-images.githubusercontent.com/61004176/216826448-812244ac-c5e4-4410-ac65-97dc0c994836.png">
</div>

## Monitoring dashboard of flights, aiplanes and their states IRT
<div align="center">
    <img src="https://user-images.githubusercontent.com/61004176/216826706-decebce0-b591-46dd-84c1-81cd466efd63.png">
</div>





