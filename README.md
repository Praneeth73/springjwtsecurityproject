# springjwtsecurityproject

Step 1: Create an endpoint that accepts userId and Password
In the above code, you can find the endpoint as /authenticate  which returns jwt as a response.

Step 2: Intercept all the incoming requests
Extract JWT from the header and validate and set it in the execution context   
