package edu.mephi.database;

public class SqlStrings {
  public String createDataBase;
  public String dropDataBase;
  public String[] insertArray;
  public String insertUnits;
  public String with;
  public String sumCountry;
  public String sumCompany;
  public String sumRegion;

  public SqlStrings() {

    with =
        new StringBuilder()
            .append("with data as (")
            .append(
                "select ( 365 * units.thermal_capacity * units.load_factor )/")
            .append("(100 * 1000 * units.burnup) as consumption, ")
            .append(" units.site from public.units where ")
            .append(" extract(year from units.commercial_operation) <> '2023'")
            .append(" and units.status = 'in operation'")
            .append(" union ")
            .append(" select units.first_load as consumption, units.site")
            .append(" from public.units")
            .append(" where ")
            .append("extract(year from units.commercial_operation) = '2023'")
            .append(" and units.status = 'in operation'")
            .append(")")
            .toString();

    sumCountry =
        new StringBuilder()
            .append(with)
            .append(
                "select country_name as Country, sum(consumption) as Consumption")
            .append(
                " from data inner join public.sites on data.site = sites.id")
            .append(
                " inner join public.countries on  sites.place = countries.id  ")
            .append(" group by country_name")
            .append(" order by consumption desc")
            .toString();

    sumCompany =
        new StringBuilder()
            .append(with)
            .append(
                "select companies_name as Company, sum(consumption) as Consumption")
            .append(
                " from data inner join public.sites on data.site = sites.id")
            .append(
                " inner join public.companies on  sites.operator = companies.id  ")
            .append(" group by companies_name")
            .append(" order by consumption desc")
            .toString();

    sumRegion =
        new StringBuilder()
            .append(with)
            .append(
                "select region_name as Region, sum(consumption) as Consumption")
            .append(
                " from data inner join public.sites on data.site = sites.id")
            .append(
                " inner join public.countries on  sites.place = countries.id  ")
            .append(
                " inner join public.regions on  countries.region_id = regions.id  ")
            .append(" group by region_name")
            .append(" order by consumption desc")
            .toString();

    String insertRegion = new StringBuilder()
                              .append("insert into public.regions (")
                              .append("id, region_name")
                              .append(") ")
                              .append("values (?, ?);")
                              .toString();

    String insertCountry =
        new StringBuilder()
            .append("insert into public.countries (")
            .append("id, country_name, subregion, region, region_id")
            .append(") ")
            .append("values (?, ?, ?, ?, ?);")
            .toString();

    String insertCompanies =
        new StringBuilder()
            .append("insert into public.companies (")
            .append("id, companies_name, full_name, country_id")
            .append(") ")
            .append("values (?, ?, ?, ?);")
            .toString();

    String insertSites =
        new StringBuilder()
            .append("insert into public.sites (")
            .append("id, npp_name, place, owner_id, operator, builder")
            .append(") ")
            .append("values (?, ?, ?, ?, ?, ?);")
            .toString();

    insertUnits =
        new StringBuilder()
            .append("insert into public.units (")
            .append("id, code, unit_name, site, status, type, model, class, ")
            .append("ru_design, operator, nsss_supplier, thermal_capacity, ")
            .append("gross_capacity, net_capacity, construction_start, ")
            .append("commercial_operation, date_shutdown, enrichment, ")
            .append("load_factor, burnup, first_load")
            .append(") ")
            .append(
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
            .toString();

    dropDataBase = new StringBuilder()
                       .append("drop table if exists public.regions cascade;")
                       .append("drop table if exists public.countries cascade;")
                       .append("drop table if exists public.companies cascade;")
                       .append("drop table if exists public.sites cascade;")
                       .append("drop table if exists public.units cascade;")
                       .toString();
    createDataBase = new StringBuilder()
                         .append("create table if not exists public.regions")
                         .append(" (")
                         .append("id integer primary key,")
                         .append("region_name varchar")
                         .append(" );")
                         .append("create table if not exists public.countries")
                         .append(" (")
                         .append("id integer primary key,")
                         .append("country_name varchar,")
                         .append("subregion varchar,")
                         .append("region varchar,")
                         .append("region_id integer references regions (id)")
                         .append(" );")
                         .append("create table if not exists public.companies")
                         .append(" (")
                         .append("id integer primary key,")
                         .append("companies_name varchar,")
                         .append("full_name varchar,")
                         .append("country_id integer references countries (id)")
                         .append(" );")
                         .append("create table if not exists public.sites")
                         .append(" (")
                         .append("id integer primary key,")
                         .append("npp_name varchar,")
                         .append("place integer references countries (id),")
                         .append("owner_id integer,")
                         .append("operator integer references companies (id),")
                         .append("builder integer")
                         .append(" );")
                         .append("create table if not exists public.units")
                         .append(" (")
                         .append("id integer primary key,")
                         .append("code varchar,")
                         .append("unit_name varchar,")
                         // .append("site integer references sites (id),")
                         .append("site integer,")
                         .append("status varchar,")
                         .append("type varchar,")
                         .append("model varchar,")
                         .append("class varchar,")
                         .append("ru_design boolean,")
                         .append("operator integer,")
                         .append("nsss_supplier integer,")
                         .append("thermal_capacity integer,")
                         .append("gross_capacity integer,")
                         .append("net_capacity integer,")
                         .append("construction_start date,")
                         .append("commercial_operation date,")
                         .append("date_shutdown date,")
                         .append("enrichment decimal,")
                         .append("load_factor integer,")
                         .append("burnup decimal,")
                         .append("first_load decimal")
                         .append(" );")
                         .toString();
    insertArray = new String[] {insertRegion, insertCountry, insertCompanies,
                                insertSites};
  }
}
