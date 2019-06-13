# Java 5 - Synchronization APIS

## Problem statement ##
Often times, application threads need to coordinate among themselves before proceeding with further execution.  Prior to Java 5, this was little cumbersome and error-prone to implement as we had to roll our own implementation to achieve inter-thread communication using the low-level synchronization APIs such as notify() and wait().

## Solution ##
An elegant way to solve the problem described above was introduced in Java 5 - Synchronization APIs, namely, CountDownLatch (CDL), CB (CyclicBarrier) and Semaphore

CDL, CB, and Semaphore are synchronization aids available out-of-the-box in JDK. They are all based on counting down from an initial value and acting (CDL and CB), blocking (Semaphore) once the count reaches zero.

## Description ##
### CDL ###
Constructed with an initial count, which will be count down by threads on which the other threads depend on before they proceed to run. 
- Dependent threads blocks on cdl.await()
- Independent threads count down by calling cdl.countDown() to decrement the initial count; which will eventually become 0.  
- When the count hits 0, dependent threads will be unblocked and it proceeds with execution.

### CB ###
- Constructed with an initial count and a callBack method.  
- Initial count will be count down by the **cooperating threads** by calling cb.await(). 
- When the count reaches 0, it indicates that all the threads had arrived at the barrier.  
    - When this happens, the callBack method passed to the CB constructor will be executed by CB itself.  

Note: Whether the initial count will be set to 0 and subsequently incremented or will it be set to initial count and count down to zero is JDK implement detail that is transparent to us.  It is still a good idea to peep under the hood.  This applies to all the APIs that we consume.

### Semaphore ###
- A semaphore is also constructed with an intial count.  
- It acts as a gate keeper by allowing the competing threads to access scarce resources only when the resources are available.  
- Availability of resource is maintained by counting down/up when threads acquire/release the resources as shown below
    - semaphore.acquire() -> use resource -> semaphore.release()
    
_With inputs from Rupesh, Ani and Aravind_
