Vue.createApp({

    data() {
        return {
            accountInfo: {},
            errorToats: null,
            errorMsg: null,
            minSearchDate: null,
            maxSearchDate: null,
            selminDate: null,
            selmaxDate: null,
            id: null
        }
    },
    methods: {
        getData: function () {
            const urlParams = new URLSearchParams(window.location.search);
            this.id = urlParams.get('id');
            axios.get(`/api/accounts/${this.id}`)
                .then((response) => {
                    //get client info
                    this.accountInfo = response.data;
                    this.minSearchDate = new Date(Math.min.apply(null, response.data.transactions.map(function(e) {return new Date(e.date)}))).toISOString().substring(0,19);
                    this.selminDate = this.minSearchDate;
                    this.maxSearchDate = new Date(Math.max.apply(null, response.data.transactions.map(function(e) {return new Date(e.date)}))).toISOString().substring(0,19);
                    this.selmaxDate = this.maxSearchDate;
                    this.accountInfo.transactions.sort(function(a, b) {return new Date(b.date) - new Date(a.date)})
                })
                .catch((error) => {
                    // handle error
                    if(error.response.status === 403) {
                        window.location.href = "/web/access-denied.html";
                    } else {
                        this.errorMsg = "Error getting data";
                        this.errorToats.show();
                    };
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
        search: function () {
            let config = {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            }
            axios.post(`/api/accounts/filterAccounts?id=${this.id}&minSearchDate=${this.selminDate}&maxSearchDate=${this.selmaxDate}`,config)
                .then(response => {
                    this.accountInfo.transactions = response.data;
                })
                .catch((error) => {
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        },
        printPdf: function() {      
            axios.get(`/api/report?id=${this.id}&minSearchDate=${this.selminDate}&maxSearchDate=${this.selmaxDate}`, {'responseType': 'blob'})
            .then((response) => {
                const blob = new Blob([response.data], { type: 'application/pdf' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = filename;
                pdf = url;
                a.click();
                // Open the PDF in a new tab or window
                //window.open(url, '_blank');
            })
            .catch((error) => {
                this.errorMsg = error;
                this.errorToats.show();
            })
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app')