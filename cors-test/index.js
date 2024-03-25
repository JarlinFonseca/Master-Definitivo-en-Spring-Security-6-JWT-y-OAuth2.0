// Example POST method implementation:
async function postData(url = "", data = {}) {
    // Default options are marked with *
    const response = await fetch(url, {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      mode: "cors", // no-cors, *cors, same-origin
      cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
      credentials: "same-origin", // include, *same-origin, omit
      headers: {
        "Content-Type": "application/json",
        // 'Content-Type': 'application/x-www-form-urlencoded',
      },
      redirect: "follow", // manual, *follow, error
      referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
      body: JSON.stringify(data), // body data type must match "Content-Type" header
    });
    return response.json(); // parses JSON response into native JavaScript objects
  }

  async function getData(url = "", jwt = null) {
    // Default options are marked with *
    const response = await fetch(url, {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      mode: "cors", // no-cors, *cors, same-origin
      cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
      credentials: "same-origin", // include, *same-origin, omit
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer ".concat(jwt),
        // 'Content-Type': 'application/x-www-form-urlencoded',
      },
      redirect: "follow", // manual, *follow, error
      referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
    });
    return response.json(); // parses JSON response into native JavaScript objects
  }
  

let loginBtn = document.getElementById('login').addEventListener('click', event => {
    console.log('login')

    postData("http://localhost:9191/api/v1/auth/authenticate", {username: 'mhernandez', password: 'clave789'})

    .then((data) => {
        console.log('authenticate. jwt: ', data.jwt);

        document.getElementById('jwt').value = data.jwt;

        return data; // JSON data parsed by `data.json()` call
    })

});


let findBtn = document.getElementById('find').addEventListener('click', event => {
    console.log('find');

    let jwt = document.getElementById('jwt').value;

        console.log('input jwt value: ', jwt)
        getData('http://localhost:9191/api/v1/products/1', jwt)
        .then(data => {
            console.log(data)
            document.getElementById('jwt').value = data.name;
        });

});