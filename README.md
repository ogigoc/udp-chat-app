# Group chat via UDP multicast

A simple Java implementation of a plaintext group chat application. It uses a
peer-to-peer (serverless) model of communication, relying on UDP multicast groups
to transfer messages back and forth.

It was built as a school project in Computer Networks.

## Building and running

This repository contains an IntelliJ IDEA project that is ready to run. Simply
start the `Main` class, and enter the required settings as prompted. Afterwards,
messages from other participants will appear on the standard output. The project
also defines unit tests for all functionality.

To send a message, simply type it into standard input.

To send a private message to another user, use `@<username> <message>`, replacing
`<username>` with the target username, and `<message>` with the text of the
private message.

To shutdown the application cleanly, use `/q` or `/quit`.


