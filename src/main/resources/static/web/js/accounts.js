Vue.createApp({
    data() {
        return {
            clientInfo: {},
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },

        addAccount: function() {
            axios.post("/api/clients/current/accounts")
            .then(response => {
                window.location.href = "/web/accounts.html"
                alert(response.data);
            })

            .catch((error) => {
                // handle error
                if(error.code==="ERR_BAD_REQUEST") {
                    this.errorMsg = error.response.data;
                } else {
                    this.errorMsg = "Error getting data";
                }
                this.errorToats.show();
            })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')