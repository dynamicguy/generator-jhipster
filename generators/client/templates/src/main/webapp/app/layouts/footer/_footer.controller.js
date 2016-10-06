(function () {
    'use strict';

    angular    
        .module(('<%=angularAppName%>'))
        .controller('FooterController', FooterController);

    FooterController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function FooterController($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isFooterCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleFooter = toggleFooter;
        vm.collapseFooter = collapseFooter;
        vm.$state = $state;

        function login() {
            collapseFooter();
            LoginService.open();
        }

        function logout() {
            collapseFooter();
            Auth.logout();
            $state.go('home');
        }

        function toggleFooter() {
            vm.isFooterCollapsed = !vm.isFooterCollapsed;
        }

        function collapseFooter() {
            vm.isFooterCollapsed = true;
        }
    }
})();
