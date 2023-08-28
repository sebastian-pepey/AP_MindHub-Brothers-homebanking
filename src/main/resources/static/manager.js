Vue.createApp({
    data() {
        return {
            email: "",
            firstName: "",
            lastName: "",
            outPut: "",
            clients: []
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        // load and display JSON sent by server for /clients
        loadData() {
            axios.get("/api/clients")
                .then((response) => {
                    // handle success
                    this.outPut = response.data;
                    this.clients = response.data;
                })
                .catch((error) => {
                    alert("Error loading clients: " + error)
                })
        },
        // handler for when user clicks add client
        addClient() {
            if (this.email.length > 1 && this.firstName.length > 1 && this.lastName.length > 1) {
                this.postClient(this.email, this.firstName, this.lastName);
            }
        },
        makeAdmin(clientEmail) {
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.patch('/api/changeAuthority', `email=${clientEmail}`, config)
            .then(response => window.location.href = "/manager.html");
        },
        // code to post a new client using AJAX
        // on success, reload and display the updated data from the server
        postClient(email, firstName, lastName, password) {
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post('/api/clients', `firstName=${firstName}&lastName=${lastName}&email=${email}&password=${password}`, config)
            .then(response => this.loadData())
                .catch((error) => {
                    // handle error
                    alert("Error to create client: " + error)
                })
        },
        clearData() {
            this.firstName = "";
            this.lastName = "";
            this.email = "";
        }
    }
}).mount("#app");