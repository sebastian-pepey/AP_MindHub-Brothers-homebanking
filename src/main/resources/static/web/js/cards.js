Vue.createApp({

    data() {
        return {
            clientInfo: {},
            creditCards: [],
            debitCards: [],
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client info
                    this.clientInfo = response.data;
                    console.log(this.clientInfo.cards);
                    this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDIT" && card.active == true);
                    this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBIT" && card.active == true);
                })
                .catch((error) => {
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        deleteCard: function(cardToDelete) {
            axios.patch(`/api/client/current/cards/delete?cardNumber=${cardToDelete}`)
                .then((response) => {
                    this.getData();
                })
                .catch((error) => {
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')